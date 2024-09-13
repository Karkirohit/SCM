
document.querySelector("#picture").addEventListener("change",function(event){
    let file= event.target.files[0];
    let reader=new FileReader();
    reader.onload=function(){
        document.querySelector("#uplode_Image_preview").setAttribute("src",reader.result);
    };
    reader.readAsDataURL(file);
});