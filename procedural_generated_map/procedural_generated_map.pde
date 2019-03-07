
World world = new World();
void setup(){
    size(400, 400);
}

void draw(){
    world.draw();
    world.addOffset((mouseX - width/2)*0.0001, (mouseY - height/2)*0.0001);
}
