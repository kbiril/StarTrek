"use strict";
import {byId, toon, setText, verberg, verwijderChildElementenVan, setTextVoorClass} from "./util.js";

const id = sessionStorage.getItem("id");
const naam = sessionStorage.getItem("naam");
getPosById(id);
async function getPosById(id) {
    const bestellingenBody = byId("bestellingenBody");
    verwijderChildElementenVan(bestellingenBody);
    const response = await fetch(`bestellingen/${id}`);

    if (response.ok) {
        const bestellingen = await response.json();
        setTextVoorClass("naam", naam);

        if (bestellingen.length !== 0) {
            toon("tabel");
            for (const bestelling of bestellingen) {
                const tr = bestellingenBody.insertRow();
                tr.insertCell().innerText = bestelling.id;
                tr.insertCell().innerText = bestelling.omschrijving;
                tr.insertCell().innerText = bestelling.bedrag;
                const datum = new Date(bestelling.moment);
                tr.insertCell().innerText = datum.toLocaleString("nl-BE");
            }
        } else {
            toon("geenBestelling");
        }
    } else {
        toon("storing");
    }
}

function verbergFouten() {
    verberg("geenBestelling");
    verberg("storing");
}