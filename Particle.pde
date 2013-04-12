// the Particle class.
class Particle {
  PVector lastPosition; // for calculating position change (velocity)
  PVector position;
  
  PVector acceleration; 
  
  float mass = 1;
  float damping = 20;

  // An ArrayList for links, so we can have as many links as we want to this particle :)
  ArrayList links = new ArrayList();
  
  boolean pinned = false;
  PVector pinLocation = new PVector(0,0);
  
  // Particle constructor
  Particle (PVector pos) {
    position = pos.get();
    lastPosition = pos.get();
    acceleration = new PVector(0,0);
  }
  
  // The update function is used to update the physics of the particle.
  // motion is applied, and links are drawn here
  void updatePhysics (float timeStep) { // timeStep should be in elapsed seconds (deltaTime)
    // gravity:
    // f(gravity) = m * g
    PVector fg = new PVector(0, mass * gravity);
    this.applyForce(fg);
    
    
    /* Verlet Integration, WAS using http://archive.gamedev.net/reference/programming/features/verlet/ 
       however, we're using the tradition Velocity Verlet integration, because our timestep is now constant. */
    // velocity = position - lastPosition
    PVector velocity = PVector.sub(position, lastPosition);
    // apply damping: acceleration -= velocity * (damping/mass)
    acceleration.sub(PVector.mult(velocity,damping/mass)); 
    // newPosition = position + velocity + 0.5 * acceleration * deltaTime * deltaTime
    PVector nextPos = PVector.add(PVector.add(position, velocity), PVector.mult(PVector.mult(acceleration, 0.5), timeStep * timeStep));
    
    // reset variables
    lastPosition.set(position);
    position.set(nextPos);
    acceleration.set(0,0,0);
  } 
  void updateInteractions () {
    // this is where our interaction comes in.
    if (mousePressed) {
      float distanceSquared = distPointToSegmentSquared(pmouseX,pmouseY,mouseX,mouseY,position.x,position.y);
      if (mouseButton == LEFT) {
        if (distanceSquared < mouseInfluenceSize) { // remember mouseInfluenceSize was squared in setup()
          // To change the velocity of our particle, we subtract that change from the lastPosition.
          // When the physics gets integrated (see updatePhysics()), the change is calculated
          // Here, the velocity is set equal to the cursor's velocity
          lastPosition = PVector.sub(position, new PVector((mouseX-pmouseX)*mouseInfluenceScalar, (mouseY-pmouseY)*mouseInfluenceScalar));
        }
      }
      else { // if the right mouse button is clicking, we tear the cloth by removing links
        if (distanceSquared < mouseTearSize) 
          links.clear();
      }
    }
  }

  void draw () {
    // draw the links and points
    stroke(0);
    if (links.size() > 0) {
      for (int i = 0; i < links.size(); i++) {
        Link currentLink = (Link) links.get(i);
        currentLink.draw();
      }
    }
    else
      point(position.x, position.y);
  }
  /* Constraints */
  void solveConstraints () {
    /* Link Constraints */
    // Links make sure particles connected to this one is at a set distance away
    for (int i = 0; i < links.size(); i++) {
      Link currentLink = (Link) links.get(i);
      currentLink.constraintSolve();
    }
    
    /* Boundary Constraints */
    // These if statements keep the particles within the screen
    if (position.y < 1)
      position.y = 2 * (1) - position.y;
    if (position.y > height-1)
      position.y = 2 * (height - 1) - position.y;
    if (position.x > width-1)
      position.x = 2 * (width - 1) - position.x;
    if (position.x < 1)
      position.x = 2 * (1) - position.x;
    
    /* Other Constraints */
    // make sure the particle stays in its place if it's pinned
    if (pinned)
      position.set(pinLocation);
  }
  
  // attachTo can be used to create links between this particle and other particles
  void attachTo (Particle P, float restingDist, float stiff) {
    Link lnk = new Link(this, P, restingDist, stiff);
    links.add(lnk);
  }
  void removeLink (Link lnk) {
    links.remove(lnk);
  }  
 
  void applyForce (PVector f) {
    // acceleration = (1/mass) * force
    // or
    // acceleration = force / mass
    acceleration.add(PVector.div(f, mass));
  }
  
  void pinTo (PVector location) {
    pinned = true;
    pinLocation.set(location);
  }
}
