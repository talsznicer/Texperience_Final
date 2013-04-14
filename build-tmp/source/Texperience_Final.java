import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import SimpleOpenNI.*; 
import processing.opengl.*; 
import saito.objloader.*; 
import java.util.Map; 
import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Texperience_Final extends PApplet {

//tal sznicer test





OBJModel test, moon, stars, tree, xoxoMan, xoxoCouch, twoMan, twoManArrow, sphear, stone;

SimpleOpenNI  context;
boolean       autoCalib=true;
PVector head = new PVector();

//  Sensor position relative to screen in mm
PVector sensorPosition = new PVector(0, 0, 0);
PVector defaultCameraPosition = new PVector(0, 0, 0);
PVector currentCameraPosition = defaultCameraPosition;

//opening-------------------------------------------------------------------------------------------------------

float        zoomF =0.5f;
int[]      userColors = { 
  color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color(0, 255, 255)
};
int[]      userCoMColors = { 
  color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0)
};

//end Opening------------------------------------------------------------------------------------------------------

// fabric------------------------------------------------------------------------------------------------------------------

// ArrayList particles;

// // every particle within this many pixels will be influenced by the cursor
// float mouseInfluenceSize = 15; 
// // minimum distance for tearing when user is right clicking
// float mouseTearSize = 8;
// float mouseInfluenceScalar = 1;

// // force of gravity is really 9.8, but because of scaling, we use 9.8 * 40 (392)
// // (9.8 is too small for a 1 second timestep)
// float gravity = 392; 

// // Dimensions for our curtain. These are number of particles for each direction, not actual widths and heights
// // the true width and height can be calculated by multiplying restingDistances by the curtain dimensions
// final int curtainHeight = 56;
// final int curtainWidth = 80;
// final int yStart = 25; // where will the curtain start on the y axis?
// final float restingDistances = 5;
// final float stiffnesses = 1;
// final float curtainTearSensitivity = 50; // distance the particles have to go before ripping

// // These variables are used to keep track of how much time is elapsed between each frame
// // they're used in the physics to maintain a certain level of accuracy and consistency
// // this program should run the at the same rate whether it's running at 30 FPS or 300,000 FPS
// long previousTime;
// long currentTime;
// // Delta means change. It's actually a triangular symbol, to label variables in equations
// // some programmers like to call it elapsedTime, or changeInTime. It's all a matter of preference
// // To keep the simulation accurate, we use a fixed time step.
// final int fixedDeltaTime = 15;
// float fixedDeltaTimeSeconds = (float)fixedDeltaTime / 1000.0;

// // the leftOverDeltaTime carries over change in time that isn't accounted for over to the next frame
// int leftOverDeltaTime = 0;

// // How many times are the constraints solved for per frame:
// int constraintAccuracy = 3;

// // instructional stuffs:
// PFont font;
// final int instructionLength = 3000;
// final int instructionFade = 300;
// // end fabric------------------------------------------------------------------------------------------------------------------

//My Floats
float mouseZPosition = 15500;
float zPosition;
boolean cameraOn = true;
float xoxoFall = 6000;
float treeHoleR = 1;
float treeHoleX =(random(-1000, 1000));
float treeHoleZ =(random(0, 40000));
float treeY = 0;
float wallUp = 0;
float startPosition = 30000;
float frameCounter = 0;
boolean bringWallUp = false;

public void setup() {

  // FULL SCREEN
  size(displayWidth, displayHeight, P3D);
  //size(900, 1000, P3D);

  //   // fabric------------------------------------------------------------------------------------------------------------------
  //   // we square the mouseInfluenceSize and mouseTearSize so we don't have to use squareRoot when comparing distances with this.
  //   mouseInfluenceSize *= mouseInfluenceSize; 
  //   mouseTearSize *= mouseTearSize;

  //   // create the curtain
  //   createCurtain();

  //   font = loadFont("LucidaBright-Demi-16.vlw");
  //   textFont(font);
  // end fabric------------------------------------------------------------------------------------------------------------------

  context = new SimpleOpenNI(this);

  // enable depthMap generation 
  if (context.enableDepth() == false)
  {
    println("Can't open the depthMap, maybe the camera is not connected!"); 

    if ( context.openFileRecording("C:\\Users\\tal\\Documents\\GitHub\\Texperience_Final\\data\\1.oni") == false)
    {
      println("can't find recording !!!!");
      exit();
      return;
    }
  }

  // enable skeleton generation for all joints
  context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

  //smooth();

  //opening-------------------------------------------------------------------------------------------------------------------

  // enable the scene, to get the floor
  context.enableScene();

  //end opening -------------------------------------------------------------------------------------------------------------------

  //types of rendering: LINES,POLYGON,QUADS,

  // test
  test = new OBJModel(this, "test.obj", "relative", POLYGON);
  test.enableDebug();
  test.scale(1);
  test.scale(1, -1, -1);

  // moon
  moon = new OBJModel(this, "moon.obj", "relative", POLYGON);
  moon.enableDebug();
  moon.scale(100000);
  moon.scale(1, -1, -1);

  // tree
  tree = new OBJModel(this, "tree.obj", "relative", POLYGON);
  tree.enableDebug();
  tree.scale(1);
  tree.scale(1, -1, -1);

  /*
  // stars
   stars = new OBJModel(this, "stars.obj", "relative", POLYGON);
   stars.enableDebug();
   stars.scale(100);
   stars.scale(1, -1, -1);
   */

  // xoxoMan
  xoxoMan = new OBJModel(this, "xoxoMan.obj", "relative", POLYGON);
  xoxoMan.enableDebug();
  xoxoMan.scale(1);
  xoxoMan.scale(1, -1, -1);

  //twoManArrow
  twoManArrow = new OBJModel(this, "twoManArrow.obj", "relative", POLYGON);
  twoManArrow.enableDebug();
  twoManArrow.scale(1);
  twoManArrow.scale(1, -1, -1);

  //xoxoCouch
  xoxoCouch = new OBJModel(this, "xoxoCouch.obj", "relative", POLYGON);
  xoxoCouch.enableDebug();
  xoxoCouch.scale(1);
  xoxoCouch.scale(1, -1, -1);

  // twoMan
  twoMan = new OBJModel(this, "twoMan.obj", "relative", POLYGON);
  twoMan.enableDebug();
  twoMan.scale(1);
  twoMan.scale(1, -1, -1);

  //sphear
  sphear = new OBJModel(this, "sphear.obj", "relative", POLYGON);
  sphear.enableDebug();
  sphear.scale(1);
  sphear.scale(1, -1, -1);

  //Stone
  stone = new OBJModel(this, "stone.obj", "relative", POLYGON);
  stone.enableDebug();
  stone.scale(1);
  stone.scale(1, -1, -1);
}




HashMap<Integer, ArrayList> hm = new HashMap();

public void draw() {

  loop();
  
  // update the cam
  context.update();

  // draw the skeleton if it's available
  int[] userList = context.getUsers();
  int numTreckedUsers = 0;
  for (int i=0;i<userList.length;i++)  
  {
    if (context.isTrackingSkeleton(userList[i]))
    {
      if (state == START)
      {
        if (!hm.containsKey(userList[i]))
        {    
          hm.put(userList[i], new ArrayList());
        }

        ArrayList<PVector> headHistory = hm.get(userList[i]);
        headHistory.add(new PVector(head.x, head.y, head.z));
        final int historySize = 50;

        if (headHistory.size() > historySize)
        {
          //check if not moving
          float total = 0; //accumulated head movement
          for (int j=1;j<historySize;j++)
          {
            PVector v3 = PVector.sub(headHistory.get(j), headHistory.get(j-1));
            total += v3.mag();
          }

          float avg = total / historySize;

          //println("head movement average" + avg);

          final int headMovementThreshold = 10; 
          if (avg < headMovementThreshold)
          {
            engage(userList[i]);
            println("engaged");
          }

          //finally, pop
          headHistory.remove(0);
        }
      }

      numTreckedUsers++;
      context.getJointPositionSkeleton(userList[i], SimpleOpenNI.SKEL_HEAD, head);
      head.x = -head.x;
      head.y = -head.y; 

      //println(head);
    }
  }

  beginCamera();
  PVector target = new PVector();
  if (numTreckedUsers > 0)
  {
    target = head;
    target.z *= 3;
  }
  else {
    target = defaultCameraPosition ;
  }

  currentCameraPosition.lerp(target, 0.1f);

  //Toggle between camera and mouse
  if (cameraOn) {
    if (state == STARTWALK) { 
      camera( 
      currentCameraPosition.x + sensorPosition.x, currentCameraPosition.y + sensorPosition.y, currentCameraPosition.z + sensorPosition.z, 
      0, 0, 0, 
      0, 1.0f, 0);
     zPosition =currentCameraPosition.z + sensorPosition.z; 


      // println("X: "+ currentCameraPosition.x);
      // println("y: "+ currentCameraPosition.y);
      // println("z: "+ currentCameraPosition.z);
      // println("______________________");
    } 
    else {
      camera( 
      60, -1000, 19000, 
      0, -5000, 0, 
      0, 1.0f, 0);
      zPosition = 19000; 
    }
  }
  else if (cameraOn == false) {
    camera( 
    (((PApplet.parseFloat(mouseX) / width) - 0.5f) * 2000), (((PApplet.parseFloat(mouseY) / height) - 0.5f) * 2000), mouseZPosition, //mouseY / height * 2000, //move camera
    0, 0, 0, 
    0, 1.0f, 0);
    zPosition = mouseZPosition ;
    //println("user z position"+ zPosition);
  }  

  scale(1, -1, 1);
  background(10, 10, 40);
  //  lights();
  //directionalLight(255, 255, 255, 0, -1, 0);
  //  directionalLight(255, 255, 255, 0, 0, 1);
  //  directionalLight(255, 255, 255, 1, 0, 0);

  //spotLight(51, 102, 126, 80, 20, 40, -1, 0, 0, PI/2, 2); 
  //pointLight(255, 255, 255, 1000, 1000,-10000);
  //ambientLight(255,255,255,0,-100000,-1);

  //directionalLight(255, 255, 255, -1, 0, 0);
  //directionalLight(255, 255, 255, 0, -1, 0);
  //directionalLight(255, 255, 255, 0, 0, -1);

  perspective(PI / 3, PApplet.parseFloat(width)/PApplet.parseFloat(height), 1, 1000000);

  ////opening-------------------------------------------------------------------------------------------------------------------
  if (state == START || state == ENGAGE) {

    scale(zoomF);
    int[]   depthMap = context.depthMap();
    int     steps   = 5;  // to speed up the drawing, draw every third point
    int     index;
    PVector realWorldPoint;

    pushMatrix();
    pushStyle();  
    scale(1);
    translate(0, 1300, startPosition + 3000);  // set the rotation center of the scene 1000 infront of the camera
    rotateY(radians(180));
    int userCount = context.getNumberOfUsers();
    int[] userMap = null;
    if (userCount > 0)
    {
      userMap = context.getUsersPixels(SimpleOpenNI.USERS_ALL);
    }

    for (int y=0;y < context.depthHeight();y+=steps)
    {
      for (int x=0;x < context.depthWidth();x+=steps)
      {
        index = x + y * context.depthWidth();
        if (depthMap[index] > 0)
        { 
          // get the realworld points
          realWorldPoint = context.depthMapRealWorld()[index];

          // check if there is a user
          if (userMap != null && userMap[index] != 0)
          {  // call the user color
            int colorIndex = userMap[index] % userColors.length;
            strokeWeight(4);
            if (state == ENGAGE || state == START)
            {
              if ( userMap[index] == chosenUser) {
                //color of chosen person
                stroke(color(0, 255, 0));
              }
              else {
                //color of recognized people
                stroke(color(0, 0, 255));
              }
            }
            else {
              //stroke(userColors[0]);
              stroke(color(0, 0, 255));
            }
          }
          else
            // camera capture background color
            stroke(0); 
          float blur = random(-100, 100);
          //float blur = 0.0;
          point(realWorldPoint.x+blur, realWorldPoint.y+blur, realWorldPoint.z);
        }
      }
    }
    popStyle();
    popMatrix();
  }
  //end opening -------------------------------------------------------------------------------------------------------------------

  // //XYZ AXIS
  // pushMatrix();
  // pushStyle();  
  // int A = 10000;
  // strokeWeight(1);
  // //X green
  // stroke(255, 0, 0);
  // line(0, 0, 0, A, 0, 0);
  // //y blue
  // stroke(0, 255, 0);
  // line(0, 0, 0, 0, A, 0);
  // //Z red
  // stroke(0, 0, 255);
  // line(0, 0, 0, 0, 0, A);
  // popStyle();
  // popMatrix();

  /*
  // test
   pushMatrix();
   pushStyle();
   translate(0, 0, 0);
   test.draw();
   fill(255, 0, 0);
   stroke(100, 0, 0);
   popStyle();  
   popMatrix();
   */

  //Begining wall
  pushMatrix();
  pushStyle();
  translate(-50000, wallUp, startPosition);
  fill(0);
  rect(0, 0, 100000, 100000);
  popStyle();  
  popMatrix();

  //Tree holes
  pushMatrix();
  pushStyle();
  translate(0, 1, 3000);
  rotateX(radians(90));
  fill (0);
  noStroke(); 
  ellipse(0, 0, treeHoleR, treeHoleR);
  popStyle();  
  popMatrix();  

  // tree
  pushMatrix();
  pushStyle(); 
  translate(0, treeY, 3000);
  tree.draw();  
  popStyle();
  popMatrix();

  //floor
  pushMatrix();
  pushStyle();  
  translate(-100000, 0, -10000);
  rotateX(radians(90));
  fill(20);
  rect(0, 0, 200000, 200000);
  popStyle();
  popMatrix();

  // moon
  pushMatrix();
  pushStyle(); 
  translate(0, 2000, -90000);
  moon.draw();  
  popStyle();
  popMatrix();  

  // xoxoMan
  pushMatrix();
  pushStyle(); 
  strokeWeight(4);
  stroke(255, 0, 0);
  translate(0, xoxoFall*1.5f, zPosition - 7500);
  xoxoMan.draw();  
  popStyle();
  popMatrix();

  // xoxoCouch
  pushMatrix();
  pushStyle(); 
  strokeWeight(1);
  stroke(255, 0, 0);
  translate(0, xoxoFall, zPosition - 7500);
  xoxoCouch.draw();  
  popStyle();
  popMatrix();

  // twoMan
  pushMatrix();
  pushStyle();
  translate(0, 0, 0);
  rotateY(radians(zPosition)/20);
  //rotateY(radians(currentCameraPosition.z + sensorPosition.z)/20);
  twoMan.draw();
  popStyle();  
  popMatrix();

  //twoManArrow
  pushMatrix();
  pushStyle();
  translate(0, 0, 0);
  rotateY(radians(zPosition)/-10);
  //rotateY(radians(currentCameraPosition.z + sensorPosition.z)/20);
  twoManArrow.draw();
  fill(255, 0, 0);
  popStyle();  
  popMatrix();

  //sphear
  pushMatrix();
  pushStyle();
  translate(-300, 600, 250);
  sphear.draw();
  popStyle();  
  popMatrix();

  //stone
  pushMatrix();
  pushStyle();
  translate(0, 0, -25000);
  stone.draw();
  popStyle();  
  popMatrix();

  /*
  // SUPER COOL
   pushMatrix();
   pushStyle(); 
   strokeWeight(4);
   stroke(255, 0, 0);
   rotateY(radians(zPosition)/-10);
   rotateX(radians(zPosition)/-10);
   translate(0, pos*1.5, 5700);
   xoxoMan.draw();  
   popStyle();
   popMatrix();
   */


  //sphere
  //  int N = 7;
  //
  //  for (int i = 0; i < N; i++)
  //    for (int j = 0; j < N; j++)
  //      for (int k = 0; k < N; k++)
  //      {
  //        pushMatrix();
  //        fill(255 * i / N, 255 * j / N, 255 * k / N);
  //        translate((i-N/2)*500, (j-N/2)*500, (k-N/2)*500);
  //        box(50);
  //        popMatrix();
  //      }

  /*
  //stars
   pushMatrix();
   pushStyle(); 
   translate(0, -300, -900000);
   scale(1000);
   stars.draw();  
   popStyle();
   popMatrix();
   */

  endCamera();

  //   // fabric------------------------------------------------------------------------------------------------------------------
  //   /******** Physics ********/
  //   // time related stuff
  //   currentTime = millis();
  //   // deltaTimeMS: change in time in milliseconds since last frame
  //   long deltaTimeMS = currentTime - previousTime;
  //   previousTime = currentTime; // reset previousTime
  //   // timeStepAmt will be how many of our fixedDeltaTime's can fit in the physics for this frame. 
  //   int timeStepAmt = (int)((float)(deltaTimeMS + leftOverDeltaTime) / (float)fixedDeltaTime);
  //   // Here we cap the timeStepAmt to prevent the iteration count from getting too high and exploding
  //   timeStepAmt = min(timeStepAmt, 5);

  //   leftOverDeltaTime += (int)deltaTimeMS - (timeStepAmt * fixedDeltaTime); // add to the leftOverDeltaTime.

  //   // If the mouse is pressing, it's influence will be spread out over every iteration in equal parts.
  //   // This keeps the program from exploding from user interaction if the timeStepAmt gets too high.
  //   mouseInfluenceScalar = 1.0 / timeStepAmt;

  //   // update physics
  //   for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
  //     // solve the constraints multiple times
  //     // the more it's solved, the more accurate.
  //     for (int x = 0; x < constraintAccuracy; x++) {
  //       for (int i = 0; i < particles.size(); i++) {
  //         Particle particle = (Particle) particles.get(i);
  //         particle.solveConstraints();
  //       }
  //     }

  //     // update each particle's position
  //     for (int i = 0; i < particles.size(); i++) {
  //       Particle particle = (Particle) particles.get(i);
  //       particle.updateInteractions();
  //       particle.updatePhysics(fixedDeltaTimeSeconds);
  //     }
  //   }
  //   // draw each particle or its links
  //   for (int i = 0; i < particles.size(); i++) {
  //     Particle particle = (Particle) particles.get(i);
  //     particle.draw();
  //   }

  //   if (millis() < instructionLength)
  //     drawInstructions();

  //    // if (frameCount % 60 == 0)
  //    //   println("Frame rate is " + frameRate);
  // }

  // void createCurtain () {
  //   // We use an ArrayList instead of an array so we could add or remove particles at will.
  //   // not that it isn't possible using an array, it's just more convenient this way
  //   particles = new ArrayList();

  //   // midWidth: amount to translate the curtain along x-axis for it to be centered
  //   // (curtainWidth * restingDistances) = curtain's pixel width
  //   int midWidth = (int) (width/2 - (curtainWidth * restingDistances)/2);
  //   // Since this our fabric is basically a grid of points, we have two loops
  //   for (int y = 0; y <= curtainHeight; y++) { // due to the way particles are attached, we need the y loop on the outside
  //     for (int x = 0; x <= curtainWidth; x++) { 
  //       Particle particle = new Particle(new PVector(midWidth + x * restingDistances, y * restingDistances + yStart));

  //       // attach to 
  //       // x - 1  and
  //       // y - 1  
  //       // particle attachTo parameters: Particle particle, float restingDistance, float stiffness
  //       // try disabling the next 2 lines (the if statement and attachTo part) to create a hairy effect
  //       if (x != 0) 
  //         particle.attachTo((Particle)(particles.get(particles.size()-1)), restingDistances, stiffnesses);
  //       // the index for the particles are one dimensions, 
  //       // so we convert x,y coordinates to 1 dimension using the formula y*width+x  
  //       if (y != 0)
  //         particle.attachTo((Particle)(particles.get((y - 1) * (curtainWidth+1) + x)), restingDistances, stiffnesses);

  //       /*
  //       // shearing, presumably. Attaching invisible links diagonally between points can give our cloth stiffness.
  //        // the stiffer these are, the more our cloth acts like jello. 
  //        // these are unnecessary for me, so I keep them disabled.
  //        if ((x != 0) && (y != 0)) 
  //        particle.attachTo((Particle)(particles.get((y - 1) * (curtainWidth+1) + (x-1))), restingDistances * sqrt(2), 0.1, false);
  //        if ((x != curtainWidth) && (y != 0))
  //        particle.attachTo((Particle)(particles.get((y - 1) * (curtainWidth+1) + (x+1))), restingDistances * sqrt(2), 1, true);
  //        */

  //       // we pin the very top particles to where they are
  //       if (y == 0)
  //         particle.pinTo(particle.position);

  //       // add to particle array  
  //       particles.add(particle);
  //     }
  //   }
  // end fabric------------------------------------------------------------------------------------------------------------------
}

// SimpleOpenNI events---------------------------------------------------------------------------
public void onNewUser(int userId)
{
  println("onNewUser - userId: " + userId);
  println("  start pose detection");

  if (autoCalib)
    context.requestCalibrationSkeleton(userId, true);
  else    
    context.startPoseDetection("Psi", userId);
}

public void onLostUser(int userId)
{
  println("onLostUser - userId: " + userId);
}

public void onExitUser(int userId)
{
  println("onExitUser - userId: " + userId);
}

public void onReEnterUser(int userId)
{
  println("onReEnterUser - userId: " + userId);
}

public void onStartCalibration(int userId)
{
  println("onStartCalibration - userId: " + userId);
}

public void onEndCalibration(int userId, boolean successfull)
{
  println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

  if (successfull) 
  { 
    println("  User calibrated !!!");
    context.startTrackingSkeleton(userId);
  } 
  else 
  { 
    println("  Failed to calibrate user !!!");
    println("  Start pose detection");
    context.startPoseDetection("Psi", userId);
  }
}

public void onStartPose(String pose, int userId)
{
  println("onStartPose - userId: " + userId + ", pose: " + pose);
  println(" stop pose detection");

  context.stopPoseDetection(userId); 
  context.requestCalibrationSkeleton(userId, true);
}

public void onEndPose(String pose, int userId)
{
  println("onEndPose - userId: " + userId + ", pose: " + pose);
}

// Keyboard events -----------------------------------------------------------------
public void keyPressed() {

  if (key == '1')
  {
    startGame();
  } 
  else   if (key == '2')
  {
    engage(0);
  } 
  else   if (key == '3')
  {
    sync();
  }
  else if (key == '4') 
  {
    startWalk();
  }

  if (cameraOn == false) {
    if (keyCode == DOWN) { 
      mouseZPosition +=500;
    } 
    else if (keyCode == UP ) {
      mouseZPosition -=500;
    }
    else if (keyCode == RIGHT ) {
      wallUp +=20;
    }
    else if (keyCode == LEFT ) {
      wallUp -=40;
    }
  }
  if (keyCode == ' ' ) {
    if (cameraOn == false) {
      cameraOn = true;
      println("camera " + cameraOn);
    }
    else if (cameraOn == true) {
      cameraOn = false;
      println("camera " + cameraOn);
    }
  }
}

// fabric------------------------------------------------------------------------------------------------------------------

// void drawInstructions () {
//   float fade = 255 - (((float)millis()-(instructionLength - instructionFade)) / instructionFade) * 255;
//   stroke(0, fade);
//   fill(255, fade);
//   rect(0, 0, 200, 45);
//   fill(0, fade);
//   text("'r' : reset", 10, 20);
//   text("'g' : toggle gravity", 10, 35);
// }

// // Credit to: http://www.codeguru.com/forum/showpost.php?p=1913101&postcount=16
// float distPointToSegmentSquared (float lineX1, float lineY1, float lineX2, float lineY2, float pointX, float pointY) {
//   float vx = lineX1 - pointX;
//   float vy = lineY1 - pointY;
//   float ux = lineX2 - lineX1;
//   float uy = lineY2 - lineY1;

//   float len = ux*ux + uy*uy;
//   float det = (-vx * ux) + (-vy * uy);
//   if ((det < 0) || (det > len)) {
//     ux = lineX2 - pointX;
//     uy = lineY2 - pointY;
//     return min(vx*vx+vy*vy, ux*ux+uy*uy);
//   }

//   det = ux*vy - uy*vx;
//   return (det*det) / len;
// }
// end fabric------------------------------------------------------------------------------------------------------------------

final int START = 0;
final int ENGAGE = 1;
final int SYNC = 2;
final int STARTWALK = 3;

int state = START;

public void startGame ()
{
  state  = START;
  chosenUser = 0;
}

int chosenUser = 0;

public void engage(int id)
{
  state = ENGAGE;
  chosenUser = id;
  bringWallUp = true;
}

public void sync()
{
  state  = SYNC;
}

public void startWalk()
{
  state  = STARTWALK;
}

//--Animations-------------------
public void loop()
{
  xoxoFall();
  wallUp();
  treePop();
}

public void wallUp ()
{
  if (frameCounter < 15000 && bringWallUp)
  {
    wallUp += 40;  
    frameCounter += 1;
  } 
  else {
    bringWallUp = false;
  }
}

public void xoxoFall () // make xoxo fall in a certain point on Z axis
{
  if (zPosition <= 15000.0f && xoxoFall >= -3500 )
  {
    xoxoFall -= 90;
  }
}
 
 public void treePop ()
 {

 if (zPosition <= 15000  )//&& treeHoleR >= 100)
  {
      treeHoleR += 10;
      println("treeHoleR: "+treeHoleR);
      if (treeHoleR >= 800)
      {
        treeY -=10;
      }
  }
 }
// // The Link class is used for handling constraints between particles.
// class Link {
//   float restingDistance;
//   float stiffness;
  
//   Particle p1;
//   Particle p2;
  
//   // the scalars are how much "tug" the particles have on each other
//   // this takes into account masses and stiffness, and are set in the Link constructor
//   float scalarP1;
//   float scalarP2;
  
//   // if you want this link to be invisible, set this to false
//   boolean drawThis = true;
  
//   Link (Particle which1, Particle which2, float restingDist, float stiff) {
//     p1 = which1; // when you set one object to another, it's pretty much a reference. 
//     p2 = which2; // Anything that'll happen to p1 or p2 in here will happen to the paticles in our array
    
//     restingDistance = restingDist;
//     stiffness = stiff;
    
//     // although there are no differences in masses for the curtain, 
//     // this opens up possibilities in the future for if we were to have a fabric with particles of different weights
//     float im1 = 1 / p1.mass; // inverse mass quantities
//     float im2 = 1 / p2.mass;
//     scalarP1 = (im1 / (im1 + im2)) * stiffness;
//     scalarP2 = (im2 / (im1 + im2)) * stiffness;
//   }
  
//   void constraintSolve () {
//     // calculate the distance between the two particles
//     PVector delta = PVector.sub(p1.position, p2.position);  
//     float d = sqrt(delta.x * delta.x + delta.y * delta.y);
//     float difference = (restingDistance - d) / d;
    
//     // if the distance is more than curtainTearSensitivity, the cloth tears
//     // it would probably be better if force was calculated, but this works
//     if (d > curtainTearSensitivity) 
//       p1.removeLink(this);
    
//     // P1.position += delta * scalarP1 * difference
//     // P2.position -= delta * scalarP2 * difference
//     p1.position.add(PVector.mult(delta, scalarP1 * difference));
//     p2.position.sub(PVector.mult(delta, scalarP2 * difference));
//   }

//   void draw () {
//     if (drawThis)
//       line(p1.position.x, p1.position.y, p2.position.x, p2.position.y);
//   }
// }
// // the Particle class.
// class Particle {
//   PVector lastPosition; // for calculating position change (velocity)
//   PVector position;
  
//   PVector acceleration; 
  
//   float mass = 1;
//   float damping = 20;

//   // An ArrayList for links, so we can have as many links as we want to this particle :)
//   ArrayList links = new ArrayList();
  
//   boolean pinned = false;
//   PVector pinLocation = new PVector(0,0);
  
//   // Particle constructor
//   Particle (PVector pos) {
//     position = pos.get();
//     lastPosition = pos.get();
//     acceleration = new PVector(0,0);
//   }
  
//   // The update function is used to update the physics of the particle.
//   // motion is applied, and links are drawn here
//   void updatePhysics (float timeStep) { // timeStep should be in elapsed seconds (deltaTime)
//     // gravity:
//     // f(gravity) = m * g
//     PVector fg = new PVector(0, mass * gravity);
//     this.applyForce(fg);
    
    
//     /* Verlet Integration, WAS using http://archive.gamedev.net/reference/programming/features/verlet/ 
//        however, we're using the tradition Velocity Verlet integration, because our timestep is now constant. */
//     // velocity = position - lastPosition
//     PVector velocity = PVector.sub(position, lastPosition);
//     // apply damping: acceleration -= velocity * (damping/mass)
//     acceleration.sub(PVector.mult(velocity,damping/mass)); 
//     // newPosition = position + velocity + 0.5 * acceleration * deltaTime * deltaTime
//     PVector nextPos = PVector.add(PVector.add(position, velocity), PVector.mult(PVector.mult(acceleration, 0.5), timeStep * timeStep));
    
//     // reset variables
//     lastPosition.set(position);
//     position.set(nextPos);
//     acceleration.set(0,0,0);
//   } 
//   void updateInteractions () {
//     // this is where our interaction comes in.
//     if (mousePressed) {
//       float distanceSquared = distPointToSegmentSquared(pmouseX,pmouseY,mouseX,mouseY,position.x,position.y);
//       if (mouseButton == LEFT) {
//         if (distanceSquared < mouseInfluenceSize) { // remember mouseInfluenceSize was squared in setup()
//           // To change the velocity of our particle, we subtract that change from the lastPosition.
//           // When the physics gets integrated (see updatePhysics()), the change is calculated
//           // Here, the velocity is set equal to the cursor's velocity
//           lastPosition = PVector.sub(position, new PVector((mouseX-pmouseX)*mouseInfluenceScalar, (mouseY-pmouseY)*mouseInfluenceScalar));
//         }
//       }
//       else { // if the right mouse button is clicking, we tear the cloth by removing links
//         if (distanceSquared < mouseTearSize) 
//           links.clear();
//       }
//     }
//   }

//   void draw () {
//     // draw the links and points
//     stroke(0);
//     if (links.size() > 0) {
//       for (int i = 0; i < links.size(); i++) {
//         Link currentLink = (Link) links.get(i);
//         currentLink.draw();
//       }
//     }
//     else
//       point(position.x, position.y);
//   }
//   /* Constraints */
//   void solveConstraints () {
//     /* Link Constraints */
//     // Links make sure particles connected to this one is at a set distance away
//     for (int i = 0; i < links.size(); i++) {
//       Link currentLink = (Link) links.get(i);
//       currentLink.constraintSolve();
//     }
    
//     /* Boundary Constraints */
//     // These if statements keep the particles within the screen
//     if (position.y < 1)
//       position.y = 2 * (1) - position.y;
//     if (position.y > height-1)
//       position.y = 2 * (height - 1) - position.y;
//     if (position.x > width-1)
//       position.x = 2 * (width - 1) - position.x;
//     if (position.x < 1)
//       position.x = 2 * (1) - position.x;
    
//     /* Other Constraints */
//     // make sure the particle stays in its place if it's pinned
//     if (pinned)
//       position.set(pinLocation);
//   }
  
//   // attachTo can be used to create links between this particle and other particles
//   void attachTo (Particle P, float restingDist, float stiff) {
//     Link lnk = new Link(this, P, restingDist, stiff);
//     links.add(lnk);
//   }
//   void removeLink (Link lnk) {
//     links.remove(lnk);
//   }  
 
//   void applyForce (PVector f) {
//     // acceleration = (1/mass) * force
//     // or
//     // acceleration = force / mass
//     acceleration.add(PVector.div(f, mass));
//   }
  
//   void pinTo (PVector location) {
//     pinned = true;
//     pinLocation.set(location);
//   }
// }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Texperience_Final" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
