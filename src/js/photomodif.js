
//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function(){
    
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
        // get a local URL representation of the image blob
        var url = window.URL.createObjectURL(imageFile);
        // Now use your newly created URL!
        img.src = url;
    }

    //img.src = 'http://i.imgur.com/w1yg6qo.jpg';
    img.crossOrigin = "Anonymous";
    // MAIN function
    function pixelate(v) {

        // if in play mode use that value, else use slider value
        var size = (play ? v : blocks.value) * 0.01,

            // cache scaled width and height
            w = canvas.width * size,
            h = canvas.height * size;

        // draw original image to the scaled size
        ctx.drawImage(img, 0, 0, w, h);
        //console.log("size : " + size);
        //console.log(w);
        //console.log(h);

        // then draw that scaled image thumb back to fill canvas
        // As smoothing is off the result will be pixelated
        ctx.drawImage(canvas, 0, 0, w, h, 0, 0, canvas.width, canvas.height);
        //var i = 1;
        /*for (var x1 = 0; x1<w*1/size; x1+=1/size){
            for(var y1 = 0; y1<h*1/size; y1+=1/size){
                var r = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[0];
                var g = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[1];
                var b = ctx.getImageData(x1,y1,x1+1/size,y1+1/size).data[2];
                //console.log("rgb " + i + " " + r + " " + g + " " + b);
               // i++;
            }
        }*/
        //i = 0;
        
    }

    // This runs the demo animation to give an impression of
    // performance.
    function toggleAnim() {

        // limit blocksize as we don't want to animate tiny blocks
        var v = Math.min(25, parseInt(blocks.value, 10)),
            dx = 0.5; /// "speed"

        // toggle play flag set by button "Animate"
        play = !play;

        // and update button's text
        animate.value = play ? 'Stop' : 'Animate';

        // if in play mode start loop
        if (play === true) anim();

        // animation loop
        function anim() {

            // increase or decrease value
            v += dx;

            // if at min or max reverse delta
            if (v <= 1 || v > 25) dx = -dx;

            // pixelate image with current value
            pixelate(v);

            // loop
            if (play) requestAnimationFrame(anim);
        }
    }

    // event listeneners for slider and button
    blocks.addEventListener('change', pixelate, false);
    animate.addEventListener('click', toggleAnim, false);

    // poly-fill for requestAnmationFrame with fallback for older
    // browsers which do not support rAF.
    window.requestAnimationFrame = (function () {
        return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || function (callback) {
            window.setTimeout(callback, 1000 / 60);
        };
    })();
});
