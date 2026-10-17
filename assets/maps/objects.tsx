<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.12.2" name="objects" tilewidth="80" tileheight="112" tilecount="3" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/house.png" width="80" height="112"/>
  <objectgroup draworder="index" id="3">
   <object id="4" x="7.5" y="50.5" width="65.75" height="54.75"/>
  </objectgroup>
 </tile>
 <tile id="1" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/oak_tree.png" width="41" height="63"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="15.8696" y="42.3478">
    <polygon points="0,0 10.1739,-0.130435 10.1304,5.43478 13.2609,9.91304 8.21739,12.5652 3.17391,12.6522 -2.73913,11.5652 0,6.52174"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="2" type="GameObject">
  <properties>
   <property name="animation" value="IDLE"/>
   <property name="animationSpeed" type="float" value="1"/>
   <property name="atlasAsset" value="OBJECTS"/>
   <property name="speed" type="float" value="4"/>
  </properties>
  <image source="objects/player.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="11.087" y="18.7391" width="8.82609" height="4.30435">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
