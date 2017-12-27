canvas = document.createElement("canvas");
canvas.height="500";
canvas.width="500";
canvasDiv.appendChild(canvas);
	
context = canvas.getContext("2d"); 

var ball = new Image();
ball.src = "baju1.png";
var sudut = 0;

var ku = setInterval(function(){
  drawBall();
}, 5);

function drawBall(){
	context.save();
    context.clearRect(0,0, canvas.width, canvas.height);
    context.translate(50,50);
    context.rotate(10 * Math.PI/90);
    context.translate(-50,-50);
    context.drawImage(ball, 0,0, 100, 100);
    context.restore();
}
