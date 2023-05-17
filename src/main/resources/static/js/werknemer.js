"use strict";
import {byId, setText} from "./util.js";

const werknemer = sessionStorage.getItem("werknemer");
const werknemerJson = JSON.parse(werknemer);


const id = werknemerJson.id;
const naam = `${werknemerJson.voornaam} ${werknemerJson.familienaam}`;
setText("naam", naam);
byId("foto").src = `images/${id}.jpg`;
setText("id", `${id}`);
setText("budget", `${werknemerJson.budget}`);

sessionStorage.setItem("id", JSON.stringify(id));
sessionStorage.setItem("naam", naam);