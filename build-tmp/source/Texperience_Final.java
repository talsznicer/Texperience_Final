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
//imports






HashMap<Integer, ArrayList> hm = new HashMap();

OBJModel test, moon, stars, tree, xoxoMan, xoxoCouch, twoMan, twoManArrow, sphear, stone;

SimpleOpenNI  context;
boolean       autoCalib=true;
PVector head = new PVector();

//Sensor position relative to screen in mm
PVector sensorPosition = new PVector(0, 0, 0);
PVector defaultCameraPosition = new PVector(0, 0, 0);
PVector currentCameraPosition = defaultCameraPosition;

//Opening
float zoomF =0.5f;
int[] userColors = { color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color(0, 255, 255) };
int[] userCoMColors = { color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0) };

//My Floats
float mouseZPosition = 19000.0f;
float zPosition = 0.0f;
boolean cameraOn = true;

float xoxoFall = 6000;
//float cameraY = 0;
//boolean runOnce = true;

float treeNumber = 5;
float[] treeX = { (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f)), (random(-6000.0f, 6000.0f))};
float[] treeZ = { (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f)), (random(-400.0f, 29500.0f))};
float[] treeRotate = { (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)), (random(0.0f, 360.0f)) };
float treeHoleR = 1;
float treeY = 0;
boolean startWallUp = false;
float wallUp = 0;

float startPosition = 30000;


float lastRWPx = 0.0f;
float lastRWPy = 0.0f;
float lastRWPz = 0.0f;

//States
final int ENGAGE = 0;
final int SYNC = 1;
final int STARTWALK = 2;

int state = ENGAGE;
int chosenUser = 0;
































public void setup() {

  // FULL SCREEN
  //size(displayWidth, displayHeight, P3D);
  size(900, 1000, P3D);

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

  smooth();

  // Opening enable the scene, to get the floor
  context.enableScene();

  //3D Objects 
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
  tree = new OBJModel(this, "tree.obj", "relative", POLYGON);//LINES);
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



















public void draw() {
//beginCamera();
  println("state:     "+state);
  println("_____");
  println("cameraX:    "+(currentCameraPosition.x + sensorPosition.x));
  println("cameraY:   "+(currentCameraPosition.y + sensorPosition.y));
  println("cameraZ:   "+(currentCameraPosition.z + sensorPosition.z));
  println("_____");
  println("zPosition:  "+zPosition);  
  println("______________________");

  context.update();
  //cameraZero ();
  cameraToggle ();
  drawSkeleton();
  xoxoFall ();
  wallUp ();
  treePop ();
  scale(1, -1, 1);
  background(10, 10, 40);
  
  //  lights();
  //directionalLight(255, 255, 255, 0, -1, 0);
  //  directionalLight(255, 255, 255, 0, 0, 1);
  //  directionalLight(255, 255, 255, 1, 0, 0);

//Draw the wall projection
  drawWall ();

//Draw 3D Objects

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

  for (int t = 0; t < treeNumber; t++)
  {
    //Tree holes
    pushStyle();
    fill (0);
    noStroke(); 
    pushMatrix();
    translate(treeX[t], 1, treeZ[t]);
    rotateX(radians(90));
    ellipse(0, 0, treeHoleR, treeHoleR);
    popMatrix();  
    popStyle(); 

    // tree
    pushStyle(); 
    pushMatrix();
    translate(treeX[t], treeY, treeZ[t]);
    rotateY(radians(treeRotate[t]));
    stroke(255);
    tree.draw();  
    popMatrix();
    popStyle();
  }

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

  //sphere
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
  
  perspective(PI / 3, PApplet.parseFloat(width)/PApplet.parseFloat(height), 1, 1000000);
  //endCamera();
}
























// Voids
public void drawSkeleton(){
  // draw the skeleton if it's available
  int[] userList = context.getUsers();
  int numTreckedUsers = 0;
  for (int i=0;i<userList.length;i++)  
  {
    if (context.isTrackingSkeleton(userList[i]))
    {
      if (state == ENGAGE)
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

          final int headMovementThreshold = 15; 

          //println("avg: "+avg);

          if (avg < headMovementThreshold)
          {
            sync(userList[i]);
          }

          //finally, pop
          headHistory.remove(0);
        }
      }
    else if (state == SYNC) 
    {      
      if (currentCameraPosition.z + sensorPosition.z >= 8000.0f)
        {
        startWalk(userList[i]);
        }
    }
    else if (state == STARTWALK)
    {
     startWallUp = true; 
    }  
    
      numTreckedUsers++;
      context.getJointPositionSkeleton(userList[i], SimpleOpenNI.SKEL_HEAD, head);
      head.x = -head.x;
      head.y = -head.y; 

      //println(head);
    }
  }

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
}

public void engage ()
{
  //reset all inits and floats
 //println("ENGAGE");
  state  = ENGAGE;
  chosenUser = 0;
}

public void sync(int id)
{
  //println("SYNC");
  state = SYNC;
  chosenUser = id;
}

public void startWalk(int id)
{
  //println("STARTWALK");
  state  = STARTWALK;
  chosenUser = id;
}


public void wallUp ()
{
  if (wallUp >= 0 && wallUp <=10000 && startWallUp)
  {
    wallUp += 80;
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
  if (zPosition <= 15000 && treeHoleR <= 2200 )
  {
    treeHoleR += 10;
  }
  if (treeHoleR >= 800)
  {    
    treeY = (currentCameraPosition.y);
  }
}

/*void cameraZero ()
{
 if ( cameraY >= 1 )
 {
  cameraY = 0;
 } else {
   cameraY =  currentCameraPosition.y + sensorPosition.y;
 }
}*/

public void cameraToggle ()
{
  //Toggle between camera and mouse
  if (cameraOn) {
    if (state == SYNC || state == ENGAGE) { 

      println("camera on freez");
      camera( 
      60, -1000, startPosition, 
      0, -5000, 0, 
      0, 1.0f, 0);
      //zPosition = 19000;
    } 
    else {
      println("camera on user");
      zPosition =(currentCameraPosition.z + sensorPosition.z); 
      camera( 
      currentCameraPosition.x + sensorPosition.x, currentCameraPosition.y + sensorPosition.y, currentCameraPosition.z + sensorPosition.z, 
      0,0,0,
      0, 1.0f, 0);
      
      // println("X: "+ currentCameraPosition.x);
      // println("y: "+ currentCameraPosition.y);
      // println("z: "+ currentCameraPosition.z);
      // println("______________________");
    }
  }
  else if (cameraOn == false) {
    println("camera off");
    camera( 
    (((PApplet.parseFloat(mouseX) / width) - 0.5f) * 2000), (((PApplet.parseFloat(mouseY) / height) - 0.5f) * 2000), mouseZPosition, //mouseY / height * 2000, //move camera
    0, 0, 0, 
    0, 1.0f, 0);
    zPosition = mouseZPosition ;
  }  
}

public void drawWall()
{
  if (state == ENGAGE || state == ENGAGE) {

    scale(zoomF);
    int[]   depthMap = context.depthMap();
    int     steps   = 10;  // to speed up the drawing, draw every third point
    int     index;
    PVector realWorldPoint;

    pushMatrix();
    pushStyle();  
    scale(1);
    translate(0, 1300, (startPosition + 300));  // set the rotation center of the scene 1000 infront of the camera
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
            
            //int colorIndex = userMap[index] % userColors.length;
            strokeWeight(4);
            if (state == ENGAGE || state == SYNC)
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
          
          point(realWorldPoint.x, realWorldPoint.y, realWorldPoint.z);
          //line(realWorldPoint.x, realWorldPoint.y, realWorldPoint.z, lastRWPx,lastRWPy,lastRWPz);
          
          lastRWPx = realWorldPoint.x;
          lastRWPy = realWorldPoint.y;
          lastRWPz = realWorldPoint.z;
        }
      }
    }
    popStyle();
    popMatrix();
  }
}




























// Keyboard events
public void keyPressed() {

  if (key == '1')
  {
    engage();
  } 
  else if (key == '2')
  {
    sync(0);
  } 
  else if (key == '3')
  {
    startWalk(0);
  }

  if (cameraOn == false) {
    if (keyCode == DOWN) { 
      mouseZPosition +=500;
    } 
    else if (keyCode == UP ) {
      mouseZPosition -=500;
    }
    // else if (keyCode == RIGHT ) {
    //   tX +=10;
    // }
    // else if (keyCode == LEFT ) {
    //   tX -=10;
    // }
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


// void initTreeXYZ ()
// {
//   if (runOnce){
//   for (int tn = 0; tn < treeNumber; tn++) 
//   {
//     treeX  [tn]  = (random(-6000, 6000));
//     treeZ [tn] =  (random(-400, 29500));
//     treeRotate [tn]=  (random(0, 360));
//   }
//   runOnce = false; 
// }
// }
























// SimpleOpenNI events

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
