<template>
  <div class="container">
    <div class="form-group mb-3 ">
      <h5 class="mb-3">기업명</h5>
      <input
        type="text"
        class="form-control"
        v-model="companyName"
        :placeholder="isManual ? '기업명을 입력하세요' : '자동으로 기업명이 입력됩니다'"
        :readonly="!isManual"
      />
    </div>

    <div>
      <h5 class="mb-3">직무선택</h5>
      <el-select v-model="selectedJob" placeholder="직무를 선택하세요" class="mb-3">
        <el-option label="백엔드" value="백엔드"></el-option>
        <el-option label="프론트엔드" value="프론트엔드"></el-option>
        <el-option label="인프라" value="인프라"></el-option>
        <el-option label="DBA" value="DBA"></el-option>
        <el-option label="기획" value="기획"></el-option>
      </el-select>
    </div>
    
    <h5 class="mb-3">자기소개서 입력</h5>
    <!-- 수동 입력일 때 제목과 내용 동적으로 추가 -->
    <div v-if="isManual">
      <div v-for="(item, index) in inputItems" :key="index" class="input-item mb-3">
        <div class="form-group">
          <input type="text" class="form-control" v-model="item.title" :id="'title-' + index" placeholder="자기소개서 문항을 입력하세요">
        </div>
        <div class="divider"></div>
        <div class="form-group">
          <textarea
            class="form-control"
            v-model="item.content"
            :id="'content-' + index"
            placeholder="내용을 입력하세요"
            @input="resizeTextarea($event)"
          ></textarea>
        </div>
      </div>
      <button class="btn btn-secondary mt-3" @click="addInputItem">항목 추가하기</button>
    </div>

    <!-- 자동 입력일 때 제목은 자동으로, 내용은 사용자가 입력 -->
    <div v-else>
      <div v-for="(item, index) in autoInputItems" :key="index" class="input-item mb-3">
        <div class="form-group">
          <input type="text" class="form-control" v-model="item.title" :id="'auto-title-' + index" placeholder="자동으로 제목이 입력됩니다" readonly>
        </div>
        <div class="divider"></div>
        <div class="form-group">
          <textarea
            class="form-control"
            v-model="item.content"
            :id="'auto-content-' + index"
            placeholder="내용을 입력하세요"
            @input="resizeTextarea($event)"
          ></textarea>
        </div>
      </div>
    </div>

    <div class="text-center mt-4">
      <router-link to="/Interview/Setting">
        <button class="btn btn-primary">다음 단계</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElSelect, ElOption } from 'element-plus';
import 'element-plus/dist/index.css';

const router = useRouter();
const route = useRoute();
const selectedJob = ref('프론트엔드');
const isManual = ref(route.state?.manual ?? JSON.parse(localStorage.getItem('manual') || 'false'));
const companyName = ref(isManual.value ? '' : '자동기업명');
const inputItems = ref([{ title: '', content: '' }]);
const autoInputItems = ref([
  { title: '자동 제목 1', content: '' },
  { title: '자동 제목 2', content: '' },
  { title: '자동 제목 3', content: '' },
  { title: '자동 제목 4', content: '' },
  { title: '자동 제목 5', content: '' }
]);

onMounted(() => {
  localStorage.setItem('manual', JSON.stringify(isManual.value));
});

const addInputItem = () => {
  inputItems.value.push({ title: '', content: '' });
};

// 텍스트 영역 자동 높이 조정
const resizeTextarea = (event) => {
  const textarea = event.target;
  textarea.style.height = 'auto'; // 우선 높이를 auto로 설정하여 기존의 높이를 초기화
  textarea.style.height = `${textarea.scrollHeight}px`; // scrollHeight에 맞게 높이 조정
};
</script>

<style scoped>
.form-group {
  margin-bottom: 15px;
}

.input-item {
  padding: 0px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding-top: 10px;
}

.input-item:not(:last-child) {
  margin-bottom: 15px;
}

.input-item .form-group {
  margin-bottom: 10px;
}

.input-item .form-group input {
  border-radius: 8px;
  border: none;
  background-color: white;
  box-shadow: none;
  outline: none;
  padding: 10px;
}

.input-item .form-group textarea {
  border-radius: 8px;
  border: none;
  background-color: white;
  box-shadow: none;
  outline: none;
  padding: 10px;
  resize: none; /* 사용자가 크기 조정 불가 */
}

.divider {
  border-bottom: 1px solid #ddd;
  margin: 10px 0;
}

.btn {
  display: block;
  width: 100%;
  margin-top: 20px;
}

.text-center {
  text-align: center;
}

.btn-primary {
  width: 150px;
  margin: 0 auto;
}
</style>
