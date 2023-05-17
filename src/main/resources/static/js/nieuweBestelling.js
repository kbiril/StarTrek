"use strict";
import {byId, toon, setText, verberg, verwijderChildElementenVan, setTextVoorClass} from "./util.js";
const id = sessionStorage.getItem("id");
const naam = sessionStorage.getItem("naam");

setTextVoorClass("naam", naam);
byId("bestel").onclick = async function () {
    verbergFouten();
    const omschrijvingInput = byId("omschrijving");
    const bedragInput = byId("bedrag");

    if (!omschrijvingInput.checkValidity()) {
        toon("omschrijvingFout");
        omschrijvingInput.focus();
        return;
    }

    if (!bedragInput.checkValidity()) {
        toon("bedragFout")
        bedragInput.focus();
        return;
    }

    const nieuweBestelling = {

        omschrijving: omschrijvingInput.value,
        bedrag: bedragInput.value
    };
        bestel(id, nieuweBestelling);
}


function verbergFouten () {
    verberg("budgetFout");
    verberg("storing");
    verberg("bedragFout");
    verberg("omschrijvingFout");
}

async function bestel(id, nieuweBestelling) {
    const response = await fetch(`bestellingen/${id}`,
        {
                method: "PATCH",
                headers: {'Content-Type': "application/json"},
                body: JSON.stringify(nieuweBestelling)
            }
        );


    if (response.ok) {
        window.location = "bestellingen.html";
        const werknemer = sessionStorage.getItem("werknemer");
        const werknemerJson = JSON.parse(werknemer);
        werknemerJson.budget -= nieuweBestelling.bedrag;
        sessionStorage.setItem("werknemer", JSON.stringify(werknemerJson));
    } else {
        const responseBody = await response.json();
        if (response.status === 409) {
            setText("budgetFout", responseBody.message);
            toon("budgetFout")
        } else {

            if (response.status === 404) {
                setText("werknemerIdFout", responseBody.message);
                toon("werknemerIdFout");

            } else {
                toon("storing");
            }
        }
    }
}

