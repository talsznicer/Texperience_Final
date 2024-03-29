//tal sznicer

//imports
import SimpleOpenNI.*;
import processing.opengl.*;
import saito.objloader.*;
import java.util.Map;
import java.util.ArrayList;

HashMap<Integer, ArrayList> hm = new HashMap();

OBJModel test, moon, stars, tree, xoxoMan, xoxoCouch, twoMan, twoManArrow, sphear, stone;

SimpleOpenNI context;
boolean autoCalib=true;
PVector torso = new PVector();

//Sensor position relative to screen in mm
PVector sensorPosition = new PVector(0, 0, 0);
PVector defaultCameraPosition = new PVector(0, 0, 0);
PVector currentCameraPosition = defaultCameraPosition;

//My Floats
float startPosition = 35000;
float userZPosition = startPosition +11000;
float mouseZPosition = userZPosition;

boolean cameraOn = true;

float xoxoFall = 6000;
//float cameraY = 0;
//boolean runOnce = true;

float treeNumber = 5;
float[] treeX = { 
  (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0)), (random(-6000.0, 6000.0))
};
float[] treeZ = { 
  (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0)), (random(-400.0, 29500.0))
};
float[] treeRotate = { 
  (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0)), (random(0.0, 360.0))
};
float treeHoleR = 1;
float treeY = 0;
boolean startWallUp = false;
float wallUp = 0;

color[] userColors = { 
  color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color (0, 0, 0), color(0, 255, 255)
};
color[] userCoMColors = { 
  color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0), color (255, 0, 0)
};

float lastRWPx = 0.0;
float lastRWPy = 0.0;
float lastRWPz = 0.0;

//States
final int ENGAGE = 0;
final int SYNC = 1;
final int PREWALK = 2;
final int STARTWALK = 3;

int state = ENGAGE;
int chosenUser = 0;
































void setup() {

  // FULL SCREEN
  size(displayWidth, displayHeight, P3D);
  //size(900, 1000, P3D);

  context = new SimpleOpenNI(this);

  enableDepthMap();

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



















void draw() {

  background(10, 10, 40);
  //printInfo();

  context.update();
  //cameraZero ();
  cameraToggle ();
  stateMannager();
  xoxoFall ();
  wallUp ();
  treePop ();
  scale(1, -1, 1);


  //  lights();
  //directionalLight(255, 255, 255, 0, -1, 0);
  //  directionalLight(255, 255, 255, 0, 0, 1);
  //  directionalLight(255, 255, 255, 1, 0, 0);

  //Draw the wall projection
  drawPrimeSence ();

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
  translate(0, xoxoFall*1.5, userZPosition - 7500);
  xoxoMan.draw();  
  popStyle();
  popMatrix();

  // xoxoCouch
  pushMatrix();
  pushStyle(); 
  strokeWeight(1);
  stroke(255, 0, 0);
  translate(0, xoxoFall, userZPosition - 7500);
  xoxoCouch.draw();  
  popStyle();
  popMatrix();

  // twoMan
  pushMatrix();
  pushStyle();
  translate(0, 0, 0);
  rotateY(radians(userZPosition)/20);
  //rotateY(radians(currentCameraPosition.z + sensorPosition.z)/20);
  twoMan.draw();
  popStyle();  
  popMatrix();

  //twoManArrow
  pushMatrix();
  pushStyle();
  translate(0, 0, 0);
  rotateY(radians(userZPosition)/-10);
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

  perspective(PI / 3, float(width)/float(height), 1, 1000000);
  //endCamera();
}
























// Voids
void enableDepthMap()
{
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
}

void printInfo()
{
  //beginCamera();
  println("state:     "+state);
  println("_____");
  println("cameraX:    "+(currentCameraPosition.x + sensorPosition.x));
  println("cameraY:   "+(currentCameraPosition.y + sensorPosition.y));
  println("cameraZ:   "+(currentCameraPosition.z + sensorPosition.z));
  println("_____");
  println("startPosition: "+startPosition);
  println("userZPosition:  "+userZPosition);  
  println("mouseZPosition: "+mouseZPosition);
  println("______________________");
}

void stateMannager() {
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

        ArrayList<PVector> torsoHistory = hm.get(userList[i]);
        torsoHistory.add(new PVector(torso.x, torso.y, torso.z));
        final int historySize = 50;

        if (torsoHistory.size() > historySize)
        {
          //check if not moving
          float total = 0; //accumulated torso movement
          for (int j=1;j<historySize;j++)
          {
            PVector v3 = PVector.sub(torsoHistory.get(j), torsoHistory.get(j-1));
            total += v3.mag();
          }

          float avg = total / historySize;

          //println("torso movement average" + avg);

          final int torsoMovementThreshold = 20; 

          //println("avg: "+avg);
          println("avg: "+avg);
          if (avg < torsoMovementThreshold)
          {
            println("SYNCED");
            sync(userList[i]);
          }

          //finally, pop
          torsoHistory.remove(0);
        }
      }
      else if (state == SYNC) 
      { 
      println("in sync state");     
        if (currentCameraPosition.z + sensorPosition.z >= 8000.0)
        {
        println("startWallUp: "+startWallUp);
        startWallUp = true;
          //startWalk(userList[i]);
        }
      }
      else if (state == PREWALK)
      {
       startWalk(userList[i]); 
      }
      else if (state == STARTWALK)
      {
        println("bla");
      }  

      numTreckedUsers++;
      context.getJointPositionSkeleton(userList[i], SimpleOpenNI.SKEL_TORSO, torso);
      torso.x = -torso.x;
      torso.y = -torso.y; 
      //println(torso);
    }
  }

  PVector target = new PVector();
  if (numTreckedUsers > 0)
  {
    target = torso;
    target.z *= 3;
  }
  else {
    target = defaultCameraPosition ;
  }
  currentCameraPosition.lerp(target, 0.1);
}

void engage ()
{
  //reset all inits and floats
  //println("ENGAGE");
  state  = ENGAGE;
  chosenUser = 0;
}

void sync(int id)
{
  //println("SYNC");
  state = SYNC;
  chosenUser = id;
}

void preWalk (int id)
{
  state = PREWALK;
  chosenUser = id;
}

void startWalk(int id)
{
  //println("STARTWALK");
  state  = STARTWALK;
  chosenUser = id;
}


void wallUp ()
{
  if (startWallUp == true)//wallUp >= 0 && wallUp <=10000 &&
  { 
    println("wallUp");
    wallUp += 80;
  }
}

void xoxoFall () // make xoxo fall in a certain point on Z axis
{
  if (userZPosition <= 15000.0 && xoxoFall >= -3500 )
  {
    xoxoFall -= 90;
  }
}

void treePop ()
{
  if (userZPosition <= 15000 && treeHoleR <= 2200 )
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

void cameraToggle ()
{
  //Toggle between camera and mouse
  if (cameraOn) {
    if (state == SYNC || state == ENGAGE || state == PREWALK) {  
      println("camera on freez");
      userZPosition = startPosition +11000;
      
      camera( 
      0, -5000, userZPosition, 
      0, -5000, 0, 
      0, 1.0, 0);
    } 
    else {
      println("camera on user");
      userZPosition = (currentCameraPosition.z + sensorPosition.z); 
      camera( 
      currentCameraPosition.x + sensorPosition.x, currentCameraPosition.y + sensorPosition.y, userZPosition, 
      0, 0, 0, 
      0, 1.0, 0);

      // println("X: "+ currentCameraPosition.x);
      // println("y: "+ currentCameraPosition.y);
      // println("z: "+ currentCameraPosition.z);
      // println("______________________");
    }
  }
  else if (cameraOn == false) {

    //println("camera off");
    userZPosition = mouseZPosition;
    camera( 
    (((float(mouseX) / width) - 0.5) * 2000), (((float(mouseY) / height) - 0.5) * 8000), userZPosition, 
    (((float(mouseX) / width) - 0.5) * 2000), (((float(mouseY) / height) - 0.5) * 8000), 0, 
    //0, 0, 0, 
    0, 1.0, 0);
  }
}

void drawPrimeSence()
{
  if (state == ENGAGE || state == SYNC || state == PREWALK) {

    int[]   depthMap = context.depthMap();
    int     steps   = 3;  // to speed up the drawing, draw every third point
    int     index;
    PVector realWorldPoint;

    pushMatrix();
    pushStyle();  
    scale(1.4, 1.4, 1);
    translate(0, 3800, (startPosition + 10000));  // set the rotation center of the scene 1000 infront of the camera
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

          strokeWeight(4);

          // check if there is a user
          if (userMap != null && userMap[index] != 0)
          {             
            if ( userMap[index] == chosenUser) {
              //color of chosen user
              stroke(color(0, 255, 0));
            }
            else 
            {
              //color of recognized user
              stroke(color(0, 0, 255));
            }
          }
          else{
            // color of camera capture
            stroke(0,0,0,0); 
            }
          point(realWorldPoint.x, realWorldPoint.y, realWorldPoint.z);

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
void keyPressed() {

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
  else if ( key == 'p') 
  {
    printInfo();
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

