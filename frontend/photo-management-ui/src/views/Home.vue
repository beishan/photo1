<template>
  <div class="home">
    <el-row :gutter="20" class="welcome-section">
      <el-col :span="24">
        <h1>欢迎使用照片管理系统</h1>
        <p>轻松管理和分享您的照片</p>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="features-section">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <h3>照片管理</h3>
            </div>
          </template>
          <div class="card-content">
            <el-button type="primary" @click="$router.push('/photos')">
              开始上传
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <h3>相册整理</h3>
            </div>
          </template>
          <div class="card-content">
            <el-button type="primary" @click="$router.push('/albums')">
              查看相册
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <h3>时间线</h3>
            </div>
          </template>
          <div class="card-content">
            <el-button type="primary" @click="$router.push('/timeline')">
              浏览时间线
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.home {
  padding: 20px;

  .welcome-section {
    text-align: center;
    margin-bottom: 40px;

    h1 {
      font-size: 2.5rem;
      margin-bottom: 1rem;
    }

    p {
      font-size: 1.2rem;
      color: #666;
    }
  }

  .features-section {
    .el-card {
      margin-bottom: 20px;
      
      .card-header {
        text-align: center;
      }

      .card-content {
        text-align: center;
        padding: 20px;
      }
    }
  }
}
</style> 