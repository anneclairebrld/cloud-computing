//    var imageFile = document.getElementById('myImage').innerHTML;
$(document).ready(function() {
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
            timeout: 10000,
            async: true,
            error: function(error){
                console.log("Error: " + error);
            },
            success: function(res){
                if(res){
                    allImages = res;
                    console.log("SUCCESS");
                    Object.keys(res).forEach( function eachKey(key)
                    {
                        getInfo(res[key]);
                    });

                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

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
            success: function(res){
                if(res){
                    addToGrid(res, id);
                }else{
                    console.log("Otherswork - FAIL : " + res);
                }
            }
        });


    }

    function addToGrid(imageInfo, id){
        var dimX = imageInfo.yNum;
        var dimY = imageInfo.xNum;
        var colorIndex = imageInfo.indexes;
        var colors = imageInfo.colors;
        var pixelH = imageInfo.pixelHeight;
        var pixelW = imageInfo.pixelWidth;
        generateMiniGrid(dimX, dimY,colorIndex,colors, pixelH,pixelW, id);
    }

    function generateMiniGrid(rows, cols, colorIndex,colors, pixelH,pixelW, id) {

            var grid = document.createElement("table");

            for ( row = 1; row <= rows; row++ ) {
                var tr = document.createElement("tr");
                for ( col = 1; col <= cols; col++ ) {
                    var td = document.createElement("td");
                    var getElem = (col-1)+(cols * (row-1));
                    var mycolor = colorIndex[getElem]-1;
                    var colorarray = colors[mycolor]; // gets the color array

                    td.style.width = 250/cols+"px";
                    td.style.height = 200/rows+"px";
                    td.style.background = ('rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+')'); // You change the color that you clicked on here
                    tr.append(td);
                }
                grid.append(tr);
            }
            document.getElementById("pictureDisplay").append(grid);
            grid.onclick = function () {
                connect(id);
                generateGrid(rows, cols, colorIndex,colors, pixelH,pixelW, id, stompClient);
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
        document.getElementById("tableContainer").style.display = "block";
        $( "#tableContainer" ).append( indexes( dimX, dimY,colorIndex) );
        $('td').css('height', pixelH);
        $('td').css('width', pixelW);
        $( "td" ).click(function() {
            var index = $( "td" ).index( this );
            $(this).empty();
            //console.log("dimX: " + dimX + "dimY: " + dimY);
            var row = Math.floor( ( index ) / dimY) + 1;
            var col = ( index % dimY ) + 1;
            //console.log("row: " + row + " col: " + col + " index: " + index);
            var mycolor = colorIndex[index]-1;
            //console.log("this color " + mycolor); // gets the index of the color in the color array
            var colorarray = colors[mycolor]; // gets the color array
            var object  = {
                dimY: dimY,
                row: row,
                col: col,
                color: [colorarray.red, colorarray.green, colorarray.blue]
            }

            socket.send('/app/interact/' + id, {}, JSON.stringify(object));
            $( this ).css( 'background-color', 'rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+ ')'); // You change the color that you clicked on here
        });
    }

    function receivedInteraction(req){
        if (req.body) {
            var interaction = JSON.parse(req.body);
            var grid = document.getElementsByTagName("td");
            var index = interaction.dimY*(interaction.row - 1) + interaction.col-1;
            grid[index].style.backgroundColor = 'rgb(' + interaction.red + ',' + interaction.green + ',' + interaction.blue + ')';
            grid[index].innerHTML = "";
        } else {
            console.log("got empty message");
        }
    }

    function indexes( rows, cols, colorIndex ) {
        var grid = "<table>";

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

