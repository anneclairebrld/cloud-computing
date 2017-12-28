$(document).ready(function() {
    var selectedColor = 0; // Global variable keeping track of which color we select
    var divInHtml = document.getElementsByTagName("DIV")[0];
    var allImages;
    if (divInHtml.id === "otherswork"){
        getImages();
    }

    function getImages(){

        $.ajax({
            url:'/otherswork',
            data:'data',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'get',
            timeout: 50000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    allImages = res;
                    console.log("SUCCESS");
                    var i = 0;
                    Object.keys(res).forEach( function eachKey(key)
                    {
                        var image = new Image();
                        image.src = key;
                        image.width = 250;
                        image.height= 200;
                        //document.body.appendChild(image);
                        addToGrid(res[key], i, image);
                        i++;
                        //getInfo(res[key], image);
                    });

                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    //var i = 0;
    function getInfo(id){

        $.ajax({
            url:'/getinfo',
            data:JSON.stringify(id),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 10000,
            async: false,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(imageInfo){
                if(imageInfo){

                    var dimX = imageInfo.yNum;
                    var dimY = imageInfo.xNum;
                    var colorIndex = imageInfo.indexes;
                    var colors = imageInfo.colors;
                    var pixelH = imageInfo.pixelHeight;
                    var pixelW = imageInfo.pixelWidth;
                    generateGrid(dimX, dimY, colorIndex,colors, pixelH,pixelW, id, stompClient);

                }else{
                    console.log("Otherswork - FAIL : " + res);
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

    var newRow = document.getElementById("pictureDisplay").insertRow(0);//(newRow);
    function addToGrid(/*imageInfo,*/ id, i, image){

        if (i != 0 && i%4 == 0){
            newRow = document.getElementById("pictureDisplay").insertRow(i/4);
        }
        newRow.insertCell(i%4).appendChild(image);
        image.onclick = function () {
        // If any of the images from otherswork is selected, it loads the coloring grid
            connect(id);
            getInfo(id);
        };

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

    function connect(socketName) {
        console.log("Interaction will start");
        var socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function () {
            stompClient.subscribe('/topic/game/' + socketName, receivedInteraction);
        });

    }
    function generateGrid(dimX, dimY, colorIndex, colors, pixelH, pixelW, id, socket) {
        document.getElementById("pictureDisplay").remove();
        document.getElementById("header1").remove();
        document.getElementById("header2").style.display = "block";
        document.getElementById("tableContainer").style.display = "inline-block";


        $( "#tableContainer" ).append( generatePalette(colors) );
        colorPalette(colors);
        $( "#palette" ).css('display','inline-block');
        $('td').css('height', pixelH);
        $('td').css('width', pixelW);
        $('td').css('cursor', 'pointer');

        $('#palette td').click(function() {
            selectedColor = $( "#palette td" ).index( this );
            console.log(selectedColor);
        });


        $( "#tableContainer" ).append( indexes( dimX, dimY,colorIndex) );
        $('td').css('height', pixelH);
        $('td').css('width', pixelW);
        $('td').css('cursor', 'pointer');
        $.ajax({
            url:'/getTrack',
            data:id,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: 'post',
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(indexes){
                for(var i in indexes){
                    var mycolor = colorIndex[indexes[i]]-1;
                    var colorarray = colors[mycolor]; // gets the color array
                    var grid = document.getElementsByTagName("td");

                    if(mycolor == selectedColor){
                        grid[indexes[i]].style.backgroundColor = 'rgb(' + colorarray.red + ',' + colorarray.green + ',' + colorarray.blue + ')';
                        grid[indexes[i]].innerHTML = "";
                    }
                }
            }
        });
        $( "td" ).click(function() {
            var index = $( "td" ).index( this );

            //var row = Math.floor( ( index ) / dimY) + 1;
            //var col = ( index % dimY ) + 1;

            var mycolor = colorIndex[index]-1;

            if(mycolor == selectedColor){
                $(this).empty();
            // If the color is the same as we selected from the palette, color it otherwise do nothing
                var colorarray = colors[mycolor]; // gets the color array
                var object  = {
                    //dimY: dimY,
                    //row: row,
                    //col: col,
                    index: index,
                    color: [colorarray.red, colorarray.green, colorarray.blue]
                }
                keepTrack(id, index);
                socket.send('/app/interact/' + id, {}, JSON.stringify(object));
                $( this ).css( 'background-color', 'rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+ ')'); // You change the color that you clicked on here
            }
        });
        $('#palette td').css('height', 30);
        $('#palette td').css('width', 30);
        $('#palette').css('border', '3px solid gray');
        $('#palette').css('vertical-align', 'top');
    }

    function generatePalette(colors) {
    //Generate a table that has all the colors
        var grid = "<table id=palette>";
        console.log("Started making the palette");
        var gridSize = Math.ceil(colors.length/2);
        for (row = 1; row <= gridSize; row++ ) {
            grid += "<tr>";
            grid += "<td></td>";
            grid += "<td></td>";
            grid += "</tr>";
        }
        return grid;
    }

    function colorPalette(colors){
    // Adding colors and numbers to the palette
        var elem = document.getElementById("palette").getElementsByTagName("td");
        for ( i = 0; i <= colors.length-1; i++ ) {
            console.log(elem[i]);
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

    function indexes( rows, cols, colorIndex ) {
        var grid = "<table style='text-align: center;align=center; display:inline-block; border: 3px solid gray;'>";

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
});

