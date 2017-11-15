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
            size = 0.01
        } else if (lev == "m") {
            size = 0.15
        } else {
            size = 0.25
        }

        // cache scaled width and height
        w = canvas.width * size,
            h = canvas.height * size;

        // draw original image to the scaled size
        ctx.drawImage(img, 0, 0, w, h);
        //console.log(ctx.getImageData(0, 0, w, h));
        //console.log(canvas.toDataURL());
        // then draw that scaled image thumb back to fill canvas
        // As smoothing is off the result will be pixelated

        //Orsi, this is where the pixel rgb's are calculated, you can console out and see
        ctx.drawImage(canvas, 0, 0, w, h, 0, 0, canvas.width, canvas.height);
//        var i = 1;
//        for (var x1 = 0; x1<w*1/size; x1+=1/size){
//            for(var y1 = 0; y1<h*1/size; y1+=1/size){
//                var r = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[0];
//                var g = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[1];
//                var b = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[2];
//                console.log("rgb " + i + " " + r + " " + g + " " + b);
//                i++;
//            }
//        }
//        i = 0;

    }

    function saveImage() {
        var imageData = canvas.toDataURL();

        $.ajax({
            url:'/images',
            data:{imageBase64: imageData},
            type: 'post',
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res === "success"){
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