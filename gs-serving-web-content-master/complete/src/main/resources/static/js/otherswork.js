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
                        getInfo(res[key]);
                        //console.log(res[key]);
                    });

                }else{
                    console.log("FAIL : " + res);
                }
            }
        });
    }

    function getInfo(id){
        console.log("requested color of id: " + id);

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
                    console.log("Otherswork - SUCCESS");
                    console.log(res);
                    addToGrid(res);

                }else{
                    console.log("Otherswork - FAIL : " + res);
                }
            }
        });


    }

    function addToGrid(imageInfo){
        var div = document.createElement("div");
        div.style.width = "300px";
        div.style.height = "200px";
        div.style.background = "red";
        div.style.color = "white";
//        div.style.borderColor = "white";
//        div.style.borderWidth = "medium";
        div.innerHTML = "Hello";
//        displayPic(imageInfo);
        var dimX = imageInfo.yNum;
        var dimY = imageInfo.xNum;
        var colorIndex = imageInfo.indexes;
        var colors = imageInfo.colors;
        document.getElementById("pictureDisplay").append(generateGrid( dimX, dimY,colorIndex,colors, imageInfo.pixelHeight,imageInfo.pixelWidth));
    }

    function generateGrid(rows, cols, colorIndex,colors, pixelH,pixelW) {
            console.log("Adding new picture to otherswork");
            var grid = document.createElement("table");
//            grid.style.width = cols + "px";
//            grid.style.height = rows + "px";
            for ( row = 1; row <= rows; row++ ) {
                var tr = document.createElement("tr");
                for ( col = 1; col <= cols; col++ ) {
                    var td = document.createElement("td");
                    var getElem = (col-1)+(cols * (row-1));
                    var mycolor = colorIndex[getElem]-1;
                    var colorarray = colors[mycolor]; // gets the color array
                    td.width = pixelH *0.8+"px";
                    td.height = pixelW * 0.8+"px";
                    td.style.background = ('rgb('+ colorarray.red+','+ colorarray.green +','+ colorarray.blue+')'); // You change the color that you clicked on here
//                    td.innerHTML = colorIndex[getElem];
                    tr.append(td);

                }
                grid.append(tr);
            }
            return grid;
    }
});