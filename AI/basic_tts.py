import sys
import json
from openai import OpenAI
import os
from dotenv import load_dotenv
import mysql.connector
import playsound

# .env 파일에서 API 키와 DB 정보 로드
load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
db_host = os.getenv('DB_HOST')
db_port = int(os.getenv('DB_PORT'))
db_user = os.getenv('DB_USER')
db_password = os.getenv('DB_PASSWORD')
db_name = os.getenv('DB_NAME')

client = OpenAI(api_key=api_key)
print(f"API 키가 로드되었습니다: {api_key is not None}")


try:
    clno = int(sys.argv[1])  # 자기소개서 번호
    number = int(sys.argv[2])  # 질문 번호
except IndexError:
    print("명령줄 인자로 clno, number 값을 전달해야 합니다.")
    sys.exit(1)
except ValueError:
    print("clno, number 값은 정수여야 합니다.")
    sys.exit(1)

# 데이터베이스에서 특정 조건에 맞는 질문 가져오기
def get_question(clno, number):
    try:
        # MySQL 연결
        connection = mysql.connector.connect(
            host=db_host,
            port=db_port,
            user=db_user,
            password=db_password,
            database=db_name
        )
        cursor = connection.cursor()
        
        # 조건에 맞는 질문 가져오기
        query = """
        SELECT cq.question
        FROM cover_letter_question cq
        INNER JOIN cover_letter cl ON cq.clno = cl.clno
        WHERE cl.clno = %s AND cq.number = %s
        """
        cursor.execute(query, (clno, number))
        result = cursor.fetchone()
        
        if result:
            return result[0]  # 질문 텍스트 반환
        else:
            print("조건에 맞는 질문이 없습니다.")
            return None
        
    except mysql.connector.Error as err:
        print(f"데이터베이스 오류: {err}")
        return None
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()

# TTS
def text_to_speech(text):
    print("질문:", text)
    response = client.audio.speech.create(
        model="tts-1",
        voice="alloy",
        input=text
    )
    response.stream_to_file("question.mp3")

# 질문 가져오기 및 실행
question = get_question(clno, number)  # 조건에 맞는 질문 가져오기
if question:
    text_to_speech(question)
    playsound.playsound("question.mp3")  # 오디오 재생
    os.remove("question.mp3")  # 오디오 파일 삭제
else:
    print("TTS를 실행할 질문이 없습니다.")

