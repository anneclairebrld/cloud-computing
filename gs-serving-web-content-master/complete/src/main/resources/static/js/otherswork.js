//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function() {
    console.log("get images from database");
    getImages();
    function getImages(){

        $.ajax({
            url:'/otherswork',
            data:'data',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'get',
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS");
                    console.log(Object.keys(res));
                    Object.keys(res).forEach( function eachKey(key)
                    {
                        getColors(res[key]);
                        //console.log(res[key]);
                    });

                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    function getColors(id){
        console.log("requested color of id: " + id);
        $.ajax({
            url:'/getcolors',
            data:JSON.stringify(parseInt(id)),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 10000,
            async: false,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS");
                    console.log(res);

                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }
});