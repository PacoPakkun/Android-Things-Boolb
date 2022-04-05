<h1>Bluetooth RGB Led Lamp</h1>

<h2>Project Description</h2>
Lampa functionala cu leduri RGB controlata de un modul Arduino Nano. Placuta Arduino comunica cu o aplicatie mobila prin intermediul unui modul Bluetooth HC-05. Aplicatia ofera posibilitatea de a porni sau opri lampa, dar si de a schimba culoarea luminii emise. 

<img src="https://github.com/PacoPakkun/Android-Things-Boolb/blob/main/demo.jpg">

<h2>Hardware Requirements</h2>
<ul>
    <li>Arduino Nano - https://store.arduino.cc/products/arduino-nano</li>
    <li>HC-05 Bluetooth module - https://cleste.ro/modul-bluetooth-hc-05.html</li>
    <li>Breadboard - https://cleste.ro/breadboard-400-puncte.html</li>
    <li>Jumper wires - https://cleste.ro/65-fire-jumper.html</li>
    <li>Rezistori 100 ohm - https://cleste.ro/rezistente-1-4w.html</li>
    <li>Led-uri RGB - https://cleste.ro/led-rgb-5mm-4pini.html</li>
</ul>

<h2>Software Requirements</h2>
<ul>
    <li>Script Arduino - https://github.com/PacoPakkun/AndroidThings/blob/main/arduino.ino</li>
    <li>Aplicatie Android - https://github.com/PacoPakkun/AndroidThings/tree/main/Boolb</li>
</ul>

<h2>Setup and Build</h2>
Pentru a rula aplicatia, se vor reproduce urmatorii pasi:
<ul>
    <li>Se va incarca scriptul Arduino pe placuta Nano, folosind un cablu USB-miniUSB</li>
    <li>Se va instala aplicatia mobila pe un telefon Android compatibil</li>
    <li>Placuta Arduino trebuie sa fie conectata la o sursa de 5V</li>
    <li>Din aplicatie, utilizatorul va apasa butonul "Reconnect" pentru a se conecta la led-uri. Daca Bluetooth-ul este inactiv, acesta va trebui pornit.</li>
    <li>Utilizatorul poate apasa butoanele de On/Off si cele de diferite culori pentru a controla lumina emisa.</li>
</ul>

