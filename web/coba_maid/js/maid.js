var canvas;
var context;
var images = {};
var totalResources = 6;
var numResourcesLoaded = 0;
var fps = 1000;
var x = 200;
var y = 100;
var napasInc = 0.8;
var napasDir = 10;
var napasAmt = 0;
var napasMax = 30;
var tangan = 0;


var breathInterval = setInterval(updateBreath);

function prepareCanvas(canvasDiv, canvasWidth, canvasHeight)
{
	
	canvas = document.createElement('canvas');
	canvas.setAttribute('width', canvasWidth);
	canvas.setAttribute('height', canvasHeight);
	canvas.setAttribute('id', 'canvas');
	canvasDiv.appendChild(canvas);
	
	if(typeof G_vmlCanvasManager != 'undefined') {
		canvas = G_vmlCanvasManager.initElement(canvas);
	}
	context = canvas.getContext("2d");
	

	loadImage("rambut1");
	loadImage("rambut2");
	loadImage("rambut3");
	loadImage("rambut4");
	loadImage("rambut5");
	loadImage("muka1");
	loadImage("muka2");
	loadImage("baju1");
	loadImage("baju2");
  loadImage("tkiri1");
  loadImage("tkiri2");
  loadImage("tkanan1");
  loadImage("tkanan2");
loadImage("kkiri1");
  loadImage("kkiri2");
loadImage("kkiri2j");
  loadImage("kkanan1");
  loadImage("kkanan2");
loadImage("kkiri2j");
}

function loadImage(name) {

  images[name] = new Image();
  images[name].onload = function() { 
	  resourceLoaded();
  };
  images[name].src = "images/" + name + ".png";
}

function resourceLoaded() {

  numResourcesLoaded += 1;
  if(numResourcesLoaded === totalResources) {
    	setInterval(redraw);
  }
}

function redraw() {
				
  canvas.width = canvas.width; 
  
  rotasi(images["kkiri1"], x + 180, y +810 - napasAmt, 0);
  rotasi(images["kkiri2"], x + 180, y +912 - napasAmt, 0);
  rotasi(images["kkanan1"], x + 320, y +810 - napasAmt, 0);
  rotasi(images["kkanan2"], x + 320, y +905 - napasAmt, 0);
  rotasi(images["baju1"], x + 250, y +540 - napasAmt, 0);

  rotasi(images["baju2"], x + 250, y +440 - napasAmt, 0);
  rotasi(images["tkiri1"], x + 130, y +455 - napasAmt, 0);
  rotasi(images["tkiri2"], x + 75, y +560 - napasAmt, 0);
  rotasi(images["tkanan1"], x + 368, y +455 - napasAmt, 0);
  rotasi(images["tkanan2"], x + 420, y +550 - napasAmt, tangan);

  rotasi(images["rambut1"], x + 255, y + 220 - napasAmt, 0);
  rotasi(images["muka1"], x + 260, y + 100 - napasAmt, 0);
  rotasi(images["rambut2"], x + 250, y + 160- napasAmt, 0);
  
}



function updateBreath() { 
				
  if (napasDir === 30) {  // napas in
   	napasAmt -= napasInc;
   	if (napasAmt < -napasMax) {
	      napasDir = -1;
         
   	}
  } 
  else {  // breath out
	napasAmt += napasInc;
	if(napasAmt > napasMax) {
	  napasDir = 30;
	}
  }
}

function rotasi(image, xr, yr, angle) { 
	context.save(); 
 
	context.translate(xr, yr);
	context.rotate(angle * Math.PI/180);
	//context.translate(0, yr);
	context.drawImage(image, -(image.width/2), -(image.height/2));
 
	context.restore(); 
}

function naik() {
	tangan += 20;
}


