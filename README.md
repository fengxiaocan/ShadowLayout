# ShadowLayout
用法其余功能参照cardview 其中shadowElevation为阴影边界，shadowStartColor为阴影开始颜色，shadowEndColor为阴影结束颜色（只建议为透明#00000000）

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.fengxiaocan:ShadowLayout:v1.0.0'
	}
