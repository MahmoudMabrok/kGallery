# KGallery

Offline-first Android app that shows a list of images fetched from a REST API, with the
ability to modify captions for every image, locally add new images from the device gallery, and
resize images while maintaining their aspect ratios.


# Screens
<div>
 <img src="https://github.com/MahmoudMabrok/kGallery/assets/13488900/be19941c-cc45-4214-a4b6-7afd65c3d093" width = 20%>
 <img src="https://github.com/MahmoudMabrok/kGallery/assets/13488900/e4295519-3cbc-4a86-9eb6-c34bea1baf23" width = 20%>
 <img src="https://github.com/MahmoudMabrok/kGallery/assets/13488900/573360fe-ccd2-435c-912c-cbdb8e3369ce" width = 20%>
 <img src="https://github.com/MahmoudMabrok/kGallery/assets/13488900/af947d1c-eba7-47e7-84ff-24998d74db4a" width = 20%>
 <img src="https://github.com/MahmoudMabrok/kGallery/assets/13488900/6fa4a1d6-d605-4ce2-9edd-c2bd05e5cc8e" width = 20%>
</div>

# Features 
- load images from Marvel API 
- store and cache images and their caption locally using Room DB
- allow user to add images from gallery
- allow user to share images into app thorugh share intent


# Todo 
- [] use TinyPng api to reseize the images
- [] allow user to save image with caption locally
- [] enhance UI by provide more updates to user while updating.

# How to use 
- clone the repo
- add keys at build.gradle. kts for app  

``` diff 
        debug {
-            buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"\"")
-            buildConfigField("String", "MARVEL_PUBLIC_KEY", "\"\"")
+           buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"your public key\"")
+            buildConfigField("String", "MARVEL_PUBLIC_KEY", "\"your private\"")
        }
```
NOTE: please notice that i swaped the values. 



  

