String img_original = mpp.getImg_original();
String path = this.getClass().getClassLoader().getResource("/").getPath();
String[] paths = path.split("/WEB-INF");
//web-path
String web_path = paths[0];
File img = new File(web_path+img_original);
if(!img.exists()){
 mgp.setImg_large("/data/goods_image1/10/1/a0e27f4ce4e84e1286d8832c0f7cdff0.png");
}else{
 mgp.setImg_large(img_original);
}