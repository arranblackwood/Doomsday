# Doomsday
A game where you jump over cars and shoot aliens
<br>
<br>

### Class Structure
<img src="./Structure Diagram.svg">
<br>

### Structure
<p>Every game object has a <b>draw</b> function for rendering, and an <b>update</b> function for game logic which is then called by either the matching manager class, or the root GameView class every frame. 
<br>
<br><b>Manager classes</b> are used when there a game object is used multiple times in the same game. For example there are multiple enemies and bullets so they have manager classes.
<br>
<br>All <b>game rendering</b> is done by the GameView class, which is a subclass of JPanel which overrides the paintComponent method. 
<br>
<br>The <b>main loop</b> is a swing timer in GameView with no delay. There is no FPS limit so most game objects that move are sent the last time to render, so that it can ensure that the movement speed is fixed.</p>
