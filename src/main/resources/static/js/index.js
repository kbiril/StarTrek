"use strict";
import {byId, toon, verberg} from "./util.js";
getAllMedewerkers();
async function getAllMedewerkers() {
    verberg("storing");
    const response = await fetch("werknemers");
    if (response.ok) {
        const werknemers = await response.json();
        const werknemersUl = byId("werknemersUl");

        for (const werknemer of werknemers) {
            const li = document.createElement("li");
            const a = document.createElement("a");

            a.onclick = async function() {
                window.location = "werknemer.html";
                sessionStorage.setItem("werknemer", JSON.stringify(werknemer));
            }
            li.appendChild(a);
            werknemersUl.appendChild(li);
            a.innerText = `${werknemer.voornaam} ${werknemer.familienaam}`;
        }
    } else {
        toon("storing");
    }
}