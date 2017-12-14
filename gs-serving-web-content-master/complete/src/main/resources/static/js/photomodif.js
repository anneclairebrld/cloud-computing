//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function() {

    // (C) Ken Fyrstenberg, Epistemex, License: CC3.0-attr
    var ctx = imageDisplay.getContext('2d'),
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
            size = 0.06;
            difficulty = 10;
        } else if (lev == "m") {
            size = 0.03;
            difficulty = 15;
        } else {
            size = 0.01;
            difficulty = 20;
        }

        // cache scaled width and height
        w = imageDisplay.width * size,
            h = imageDisplay.height * size;

        // draw original image to the scaled size
        ctx.drawImage(img, 0, 0, imageDisplay.width, imageDisplay.height);
        // then draw that scaled image thumb back to fill canvas
        // As smoothing is off the result will be pixelated

        //ctx.drawImage(imageDisplay, 0, 0, w, h, 0, 0, imageDisplay.width, imageDisplay.height);
    }

    function saveImage() {
        var imageData = imageDisplay.toDataURL();
        var image = {
            imageData: imageData,

            width: imageDisplay.width,
            height: imageDisplay.height,
            pixelWidth: imageDisplay.width/w,
            pixelHeight: imageDisplay.height/h,
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
                    console.log("SUCCESS save");
                    getImage();
//                    return pixelatedImageData;
                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    function getImage(){

        $.ajax({
            url:'/coloring',
            data:'ask',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS");
                    console.log(res.indexes);
                    connect();
                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    var stompClient = null;

    function setConnected(connected) {
        $("#interaction").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        if (connected) {
            $("#interaction").show();
        }
        else {
            $("#conversation").hide();
        }
        $("#game").html("");
    }

    function connect() {
        console.log("Interaction will start :)");
        var socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (res) {
            stompClient.subscribe('/topic/game', receivedInteraction);
            prettyGrid(20,30,stompClient);
            //stompClient.send("/app/interact", {}, "HIIIII");
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    function generateGrid( rows, cols ) {
        var grid = "<table>";

        console.log("Started making the table");
        for ( row = 1; row <= rows; row++ ) {
            grid += "<tr>";
            for ( col = 1; col <= cols; col++ ) {
                var getElem = (col-1)+(cols * (row-1));
                grid += "<td>"+colorIndex[getElem]+"</td>";
            }
            grid += "</tr>";
        }
        return grid;
    }

    function prettyGrid(dimX,dimY, socket){

        $( "td" ).click(function() {
            var index = $( "td" ).index( this );
            var row = Math.floor( ( index ) / dimX) + 1;
            var col = ( index % dimY ) + 1;
            console.log("Sending...")
            var object  = {
                row: row,
                col: col,
                color: [255, 0, 0]
            }
            socket.send('/app/interact', {}, JSON.stringify(object));

            $( this ).css( 'background-color', 'red' ); // You change the color that you clicked on here
        });
    }
    function receivedInteraction(req){
        if (req.body) {
            var interaction = JSON.parse(req.body)
            console.log("got message with row and col " + interaction.row + " " + interaction.col);
        } else {
            alert("got empty message");
        }
    }

    easy.addEventListener('click', function() { pixelate("e") }, false);
    medium.addEventListener('click', function() { pixelate("m") }, false);
    hard.addEventListener('click', function() { pixelate("h") }, false);
    startColoring.addEventListener('click', function() { saveImage() }, false);
//    startColoring.addEventListener('click', function() { makeGrid(20, 20) }, false);
    //startColoring.addEventListener('click',function() {prettyGrid(20,30)}, false);
});