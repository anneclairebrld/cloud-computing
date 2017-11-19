//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function() {

    // (C) Ken Fyrstenberg, Epistemex, License: CC3.0-attr
    var ctx = canvas.getContext('2d'),
        img = new Image(),
        play = false;

    // turn off image smoothing - this will give the pixelated effect
    ctx.mozImageSmoothingEnabled = false;
    ctx.webkitImageSmoothingEnabled = false;
    ctx.imageSmoothingEnabled = false;

    // wait until image is actually available
    //img.src =  localStorage.getItem('imageurl');

    img.onload = pixelate;
    // some image, we are not struck with CORS restrictions as we
    // do not use pixel buffer to pixelate, so any image will do
    document.getElementById('myImage').onchange = function(e) {
        // Get the first file in the FileList object
        var imageFile = this.files[0];
        //console.log(imageFile.type);
        // get a local URL representation of the image blob
        var url = window.URL.createObjectURL(imageFile);
        // Now use your newly created URL!
        img.src = url;

    }

    img.crossOrigin = "Anonymous";

    function pixelate(lev) {

        // find which button was pressed
        if (lev == "e") {
            size = 0.05;
            difficulty = 5;
        } else if (lev == "m") {
            size = 0.1;
            difficulty = 10;
        } else {
            size = 0.15;
            difficulty = 15;
        }

        // cache scaled width and height
        w = canvas.width * size,
            h = canvas.height * size;

        // draw original image to the scaled size
        ctx.drawImage(img, 0, 0, w, h);
        // then draw that scaled image thumb back to fill canvas
        // As smoothing is off the result will be pixelated

        ctx.drawImage(canvas, 0, 0, w, h, 0, 0, canvas.width, canvas.height);
    }

    function saveImage() {
        var imageData = canvas.toDataURL();
        var image = {
            imageData: imageData,
            pixelWidth: canvas.width/w,
            pixelHeight: canvas.height/h,
            difficulty: difficulty
        }
        //console.log(image);
        $.ajax({
            url:'/images',
            data:JSON.stringify(image),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res.status === "success"){
                    console.log("SUCCESS");
                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    easy.addEventListener('click', function() { pixelate("e") }, false);
    medium.addEventListener('click', function() { pixelate("m") }, false);
    hard.addEventListener('click', function() { pixelate("h") }, false);
    startColoring.addEventListener('click', function() { saveImage() }, false);

});