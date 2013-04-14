//tal sznicer test

import SimpleOpenNI.*;
import processing.opengl.*;
import saito.objloader.*;

OBJModel test, moon, stars, tree, xoxoMan, xoxoCouch, twoMan, twoManArrow, sphear, stone;

SimpleOpenNI  context;
boolean       autoCalib=true;
PVector head = new PVector();

//  Sensor position relative to screen in mm
PVector sensorPosition = new PVector(0, 0, 0);
PVector defaultCameraPosition = new PVector(0, 1800, 1550);
PVector currentCameraPosition = defaultCameraPosition;

//opening-------------------------------------------------------------------------------------------------------

float        zoomF =0.5f;
float        rotX = radians(180);  // by default rotate uthe hole scene 180deg around the x-axis, 
// the data from openni comes upside down
float        rotY = radians(0);
color[]      userColors = { 
  color(255, 255, 255), color(255, 255, 255), color(255, 255, 255), color(255, 255, 255), color(255, 0, 255), color(0, 255, 255)
};
color[]      userCoMColors = { 
  color(86, 118, 255), color(86, 118, 255), color(86, 118, 255), color(86, 118, 255), color(255, 100, 255), color(100, 255, 255)
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
float mouseZPosition;
float zPosition;
boolean cameraOn = false;
float xoxoFall = 4000;
float treeHoleR = 1;
float treeHoleX =(random(-1000, 1000));
float treeHoleZ =(random(0, 40000));
boolean treeHoleSmall = false;
float openGate = 0;
float startPosition = 30000;

void setup() {

  // FULL SCREEN
  //size(displayWidth, displayHeight, P3D);
  size(900, 1000, P3D);

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

    if ( context.openFileRecording("C:\\Users\\tal\\GitHub\\FinalTexperience\\fin\\1.oni") == false)
    {
      println("can't find recording !!!!");
      exit();
      return;
    }
  }

  // enable skeleton generation for all joints
  context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

  mouseZPosition =  15500;

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

import java.util.Map;
import java.util.ArrayList;

HashMap<Integer, ArrayList> hm = new HashMap();



void draw() {

  // update the cam
  context.update();

  // draw the skeleton if it's available
  int[] userList = context.getUsers();
  int numTreckedUsers = 0;
  for (int i=0;i<userList.length;i++)  
  {
    if (context.isTrackingSkeleton(userList[i]))
    {

      if (state != ENGAGE)
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

          println("head movement average" + avg);

          final int headMovementThreshold = 10; 
          if (avg < headMovementThreshold)
          {
            engage(userList[i]);
            println("AAAAAAAAAAAAAAAAAAA");
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

  currentCameraPosition.lerp(target, 0.1);

  //Toggle between camera and mouse
  if (cameraOn) {
    camera( 
    currentCameraPosition.x + sensorPosition.x, currentCameraPosition.y + sensorPosition.y, currentCameraPosition.z + sensorPosition.z, 
    0, 0, 0, 
    0, 1.0, 0);
    zPosition =currentCameraPosition.z + sensorPosition.z; 
    println("user z position"+ zPosition);
  } 
  else {
    camera( 
    (((float(mouseX) / width) - 0.5) * 2000), (((float(mouseY) / height) - 0.5) * 2000), mouseZPosition, //mouseY / height * 2000, //move camera
    0, 0, 0, 
    0, 1.0, 0);
    zPosition = mouseZPosition ;
    println("user z position"+ zPosition);
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

  perspective(PI / 3, float(width)/float(height), 1, 1000000);

  ////opening-------------------------------------------------------------------------------------------------------------------
  if (state == START || state == ENGAGE) {

    scale(zoomF);
    int[]   depthMap = context.depthMap();
    int     steps   = 5;  // to speed up the drawing, draw every third point
    int     index;
    PVector realWorldPoint;

    pushMatrix();
    pushStyle();  
    translate(0, 1000, startPosition + 3000);  // set the rotation center of the scene 1000 infront of the camera
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
          {  // calc the user color
            //int colorIndex = userMap[index] % userColors.length;
            strokeWeight(4);
            if (state == ENGAGE)
            {
              if ( userMap[index] == chosenUser) {
                stroke(color(255, 0, 0));
              }
              else {
                stroke(color(0, 255, 0));
              }
            }
            else {
              stroke(userColors[0]);
            }
          }
          else
            // default color
            noStroke();
          stroke(255, 255, 0); 
          point(realWorldPoint.x, realWorldPoint.y, realWorldPoint.z);
        }
      }
    }
    popStyle();
    popMatrix();
  }

  //end opening -------------------------------------------------------------------------------------------------------------------

  //XYZ AXIS
  pushMatrix();
  pushStyle();  
  int A = 10000;
  strokeWeight(1);
  //X green
  stroke(255, 0, 0);
  line(0, 0, 0, A, 0, 0);
  //y blue
  stroke(0, 255, 0);
  line(0, 0, 0, 0, A, 0);
  //Z red
  stroke(0, 0, 255);
  line(0, 0, 0, 0, 0, A);
  popStyle();
  popMatrix();


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
  //rotateY(radians(180));
  translate(-50000, openGate, startPosition);
  //rotateX(radians(openGate));
  fill(0);
  rect(0, 0, 100000, 100000);
  popStyle();  
  popMatrix();

  //Falling Tree Scrip
  // if (zPosition <= 11500.0 && zPosition >= 10000.0 && treeHoleR > 0 && treeHoleSmall == false)
  // {
  //   treeHoleSmall = true;
  //   for (int k = 0; k < 15000; k++)
  //   {
  //     treeHoleR += 0.1;
  //     println (treeHoleR);
  //   }
  //   println ("done");
  // }

  //Tree holes

  pushMatrix();
  pushStyle();
  //  translate(treeHoleX, treeHoleZ, -2);
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
  //translate(treeHoleX, 500, treeHoleZ);
  translate(0, 0, 3000);
  tree.draw();  
  popStyle();
  popMatrix();

  //floor
  pushMatrix();
  pushStyle();  
  translate(-100000, 0, -10000);
  rotateX(radians(90));
  fill(255);
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

  // make xoxo fall in a certain point on Z axis
  // if (zPosition <= 12400.0 && xoxoFall >= -2500)
  // {
  //   for (int k = 0; k < 200; k++)
  //   {
  //     xoxoFall = xoxoFall-1;
  //     //print (xoxoFall);
  //   }
  // }

  // xoxoMan
  pushMatrix();
  pushStyle(); 
  strokeWeight(4);
  stroke(255, 0, 0);
  translate(0, xoxoFall*1.5, 5700);
  xoxoMan.draw();  
  popStyle();
  popMatrix();

  // xoxoCouch
  pushMatrix();
  pushStyle(); 
  strokeWeight(1);
  stroke(255, 0, 0);
  translate(0, xoxoFall, 5700);
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
void onNewUser(int userId)
{
  println("onNewUser - userId: " + userId);
  println("  start pose detection");

  if (autoCalib)
    context.requestCalibrationSkeleton(userId, true);
  else    
    context.startPoseDetection("Psi", userId);
}

void onLostUser(int userId)
{
  println("onLostUser - userId: " + userId);
}

void onExitUser(int userId)
{
  println("onExitUser - userId: " + userId);
}

void onReEnterUser(int userId)
{
  println("onReEnterUser - userId: " + userId);
}

void onStartCalibration(int userId)
{
  println("onStartCalibration - userId: " + userId);
}

void onEndCalibration(int userId, boolean successfull)
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

void onStartPose(String pose, int userId)
{
  println("onStartPose - userId: " + userId + ", pose: " + pose);
  println(" stop pose detection");

  context.stopPoseDetection(userId); 
  context.requestCalibrationSkeleton(userId, true);
}

void onEndPose(String pose, int userId)
{
  println("onEndPose - userId: " + userId + ", pose: " + pose);
}

// Keyboard events -----------------------------------------------------------------
void keyPressed() {

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

  if (cameraOn == false) {
    if (keyCode == DOWN) { 
      mouseZPosition +=500;
    } 
    else if (keyCode == UP ) {
      mouseZPosition -=500;
    }
    else if (keyCode == RIGHT ) {
      openGate +=20;
    }
    else if (keyCode == LEFT ) {
      openGate -=40;
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

int state = START;

void startGame ()
{
  chosenUser = 0;
  state  = START;
}

int chosenUser = 0;

void engage(int id)
{
  state = ENGAGE;
  chosenUser = id;
  println ("bridg UP");
}

void sync()
{
  state  = SYNC;
}

