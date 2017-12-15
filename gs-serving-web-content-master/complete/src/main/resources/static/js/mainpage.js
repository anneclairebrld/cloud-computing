$(document).ready(function(){
	/*$('.myImage').change(function(){
		var imageFile = this.files[0];
		console.log("imageFile: " + imageFile);
		var url = window.URL.createObjectURL(imageFile);
		console.log("url: " + url);
		localStorage.setItem('imageurl', url);
		//console.log("imageurl: " + imageurl);
		console.log('go to here');
		window.location = "imagePixelation.html";
	});*/

	//This is how we make buttons do cool stuff using css
//	$('.custom-file-upload').on({
//		mouseenter : function(){
//			$(this).css("background-color", "grey");
//		},
//
//		mouseleave : function(){
//			$(this).css("background-color", "#ff8000");
//		}
//	});

	$('.btn-file').on({
    	mouseenter : function(){
    		$(this).css("background-color", "grey");
   		},

    	mouseleave : function(){
    		$(this).css("background-color", "#ff8000");
    	}
    });


	$('.otherswork').on({
		mouseenter : function(){
			$(this).css("background-color", "grey");
		},

		mouseleave : function(){
			$(this).css("background-color", "#d95326");
		}
	});

});

function toggleVisibility(k) {
    var welcome  = document.getElementById("welcome");
    var coloring = document.getElementById("coloring");
    if (k == 2) coloring = document.getElementById("coloring2");
    welcome.style.display = "none";
    coloring.style.display=  "block";
}

function whiteCanvas(){
    var coloring = document.getElementById("coloring");
    var interact = document.getElementById("startInteract");
    coloring.style.display = "none";
    interact.style.display = "block";

}