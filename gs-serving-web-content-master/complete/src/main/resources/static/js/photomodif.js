$(document).ready(function() {

    var ctx = imageDisplay.getContext('2d'),
        img = new Image(),
        play = false;

    var selectedColor = -1;

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

    var links = $('input');
    links.css('font-size', '20px');

    links.click(function() {
      links.css('background-color', 'white');
      $(this).css('background-color', 'orange');
    });

    function saveImage() {
        var imageData = imageDisplay.toDataURL(); // This is where we get the image (data from the canvas)
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
                alert("Ooops, something went wrong! Try another image with more colors or check your Internet connection!");
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
        $('#loading').show();
        $.ajax({
            url:'/coloring',
            data:'ask',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 50000,
            async: true,
            error: function(error){
                console.log("Problem occurred when getting the image.")
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    console.log("SUCCESS");
                    console.log(res);
                    connect(res, socketName);
                    prettyGrid(res.pixelHeight,res.pixelWidth,res.yNum, res.xNum, res.indexes, res.colors, socketName, stompClient);
                }else{
                    console.log("FAIL : " + res);
                }
            },
            complete: function(){
                    $('#loading').hide();
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
        var grid = "<table id=coloringGrid>";

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
        $('#tableContainer').append( generateGrid( dimX, dimY,colorIndex) );
        $('#tableContainer').css('display','inline-block');
        $('#coloringGrid').css('display','inline-block');
        $('#coloringGrid').css('border', '3px solid gray');
        $('td').css('height', pixelH);
        $('td').css('width', pixelW);
        $('td').css('cursor', 'pointer');
        $( "td" ).click(function() {
            var index = $( "td" ).index( this );
//            console.log("dimX: " + dimX + "dimY: " + dimY);
            console.log("Sending...")
            var mycolor = colorIndex[index]-1;
            var colorarray = colors[mycolor]; // gets the color array
            var object  = {
                //dimY: dimY,
                index: index,
                //row: row,
                //col: col,
                color: [colorarray.red, colorarray.green, colorarray.blue]
            }
            if(mycolor == selectedColor){
                keepTrack(id, index);
                socket.send('/app/interact/' + id, {}, JSON.stringify(object));
                $( this ).css( 'background-color', 'rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+')'); // You change the color that you clicked on here
            }
        });
        $( "#tableContainer" ).append("<div id = pal ></div>")
        $( "#pal" ).append( generatePalette(colors) );

        colorPalette(colors);
        $('#palette td').click(function() {
            selectedColor = $( "#palette td" ).index( this );
            $( '#palette td' ).css('border', '1px')
            $( this ).css('border', '4px solid black');
            console.log(selectedColor);
        });

        $('#pal').css('text-align', 'center');
        $('#pal').css('align', 'center');

        $('#palette td').css('height', 30);
        $('#palette td').css('width', 30);
        $('td').css('cursor', 'pointer');
        $('#palette').css('border', '3px solid gray');
        $('#palette').css('align', 'center');
        $('#palette').css('position', 'fixed');
        $('#palette').css('bottom', 0);
    }

    function generatePalette(colors) {
        var grid = "<table id=palette>";
        console.log("Started making the palette");
        grid += "<tr>";
        for (row = 1; row <= colors.length; row++ ) {
            grid += "<td></td>";
        }
        grid += "</tr>";
        return grid;
    }

    function colorPalette(colors){
        var elem = document.getElementById("palette").getElementsByTagName("td");
        for ( i = 0; i <= colors.length-1; i++ ) {
            elem[i].innerHTML = i+1;
            elem[i].style.backgroundColor = 'rgb('+ colors[i].red+','+ colors[i].green +','+ colors[i].blue+')';
        }
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