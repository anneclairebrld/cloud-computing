//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function() {

    var ctx = imageDisplay.getContext('2d'),
        img = new Image(),
        play = false;

    ctx.mozImageSmoothingEnabled = false;
    ctx.webkitImageSmoothingEnabled = false;
    ctx.imageSmoothingEnabled = false;


    img.onload = pixelate;

    document.getElementById('myImage').onchange = function(e) {
        var imageFile = this.files[0];
        var url = window.URL.createObjectURL(imageFile);
        img.src = url;

    }

    img.crossOrigin = "Anonymous";

    function pixelate(lev) {

        if (lev == "e") {
            size = 0.04;
            difficulty = 10;
        } else if (lev == "m") {
            size = 0.02;
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

        $.ajax({
            url:'/images',
            data:JSON.stringify(image),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 50000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS save");
                    console.log(res);
                    getImage(res);
//                    return pixelatedImageData;
                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    function getImage(socketName){

        $.ajax({
            url:'/coloring',
            data:'ask',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 50000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS");
                    connect(res, socketName);
                    prettyGrid(res.pixelHeight,res.pixelWidth,res.yNum, res.xNum, res.indexes, res.colors, socketName, stompClient);
                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    function keepTrack(id, index) {
        var data = {
            id: String(id),
            index: index
        }
        console.log("data: " + JSON.stringify(data));
        $.ajax({
            url:'/track',
            data:JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 50000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("success");
                }
                else console.log("error in track");
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

    function connect(res,socketName) {
        console.log("Interaction will start :)");
        var socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (res) {
            stompClient.subscribe('/topic/game/'+socketName, receivedInteraction);
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    function generateGrid( rows, cols, colorIndex ) {
        var grid = "<table>";

        console.log("Started making the table");
        for ( row = 1; row <= rows; row++ ) {
            grid += "<tr>";
            for ( col = 1; col <= cols; col++ ) {
                var getElem = (col-1)+(cols * (row-1));
                grid += "<td>"+(colorIndex[getElem])+"</td>";
            }
            grid += "</tr>";
        }
        return grid;
    }

    function prettyGrid(pixelH,pixelW,dimX,dimY,colorIndex,colors, id, socket){
        console.log("dimX: "  + dimX + "dimY: " + dimY + "colorind: " + colorIndex);
        $( "#tableContainer" ).append( generateGrid( dimX, dimY,colorIndex) );
        $('td').css('height', pixelH);
        $('td').css('width', pixelW);
        $('td').css('cursor', 'pointer');
        $( "td" ).click(function() {
            var index = $( "td" ).index( this );
            console.log("dimX: " + dimX + "dimY: " + dimY);
            //var row = Math.floor( ( index ) / dimY) + 1;
            //var col = ( index % dimY ) + 1;
            console.log("Sending...")
            var mycolor = colorIndex[index]-1;
            console.log("this color index " + mycolor); // gets the index of the color in the color array
            var colorarray = colors[mycolor]; // gets the color array
            var object  = {
                //dimY: dimY,
                index: index,
                //row: row,
                //col: col,
                color: [colorarray.red, colorarray.green, colorarray.blue]
            }
            keepTrack(id, index);
            socket.send('/app/interact/' + id, {}, JSON.stringify(object));

            $( this ).css( 'background-color', 'rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+')'); // You change the color that you clicked on here
        });
    }
    function receivedInteraction(req){
        if (req.body) {
            var interaction = JSON.parse(req.body);
            var grid = document.getElementsByTagName("td");
            var index = interaction.index;//interaction.dimY*(interaction.row - 1) + interaction.col-1;
            grid[index].style.backgroundColor = 'rgb(' + interaction.red + ',' + interaction.green + ',' + interaction.blue + ')';
            grid[index].innerHTML = "";
        } else {
            console.log("got empty message");
        }
    }


    easy.addEventListener('click', function() { pixelate("e") }, false);
    medium.addEventListener('click', function() { pixelate("m") }, false);
    hard.addEventListener('click', function() { pixelate("h") }, false);
    startColoring.addEventListener('click', function() { saveImage() }, false);
});