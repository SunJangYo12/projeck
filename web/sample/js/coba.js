canvas = document.createElement("canvas");
canvas.height="500";
canvas.width="500";
canvasDiv.appendChild(canvas);
	
context = canvas.getContext("2d"); 
// membuat tulisan
context.font = "50px arial";
context.fillText("ini text ", 0, 100);
	
// membuat lingkara
context.beginPath();
context.arc(100,100,100,0,50);
context.stroke();
context.closePath();

// membuat persegi
context.fillRect(0,0,500,50);
