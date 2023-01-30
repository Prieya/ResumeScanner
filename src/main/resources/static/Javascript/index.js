function fileUpload(){
    var fileInput = document.getElementById("file_upload");
    var file = fileInput.files[0];
    var fileName = file.name;
    if(e.target.result != null){
        document.getElementById("resume_text").innerHTML = fileName;
    }
}

function firstClick(){
    var firstName = document.getElementById("firstName");
    var lastName = document.getElementById("firstName");
    if(firstName.value == "" && lastName.value == ""){
        document.getElementById("firstName").style.borderColor = "red";
        document.getElementById("lastName").style.borderColor = "red";
        document.getElementById("email").style.borderColor = "red";
    }else{
         document.getElementById("submit_user").submit();
         document.getElementById("resume").submit();
    }
}


$(".update_btn").click(function () {
    $("#file_upload").trigger('click');
});