import java.util.ArrayList;

World world = new World();
void setup(){
    size(800, 800);
}

void draw(){
    world.draw();
    //world.addOffset((mouseX - width/2)*0.0005, (mouseY - height/2)*0.0005);
}
