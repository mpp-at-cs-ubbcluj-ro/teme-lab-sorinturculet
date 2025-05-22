// services/api.js
const BASE_URL = "http://localhost:8080/api/participants";

export const getAllParticipants = async () =>
    fetch(BASE_URL).then(res => res.json());

export const createParticipant = async (participant) =>
    fetch(BASE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(participant),
    }).then(res => res.json());

export const updateParticipant = (participant) =>
    fetch(`${BASE_URL}/${participant.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(participant),
    });

export const deleteParticipant = async (id) =>
    fetch(`${BASE_URL}/${id}`, { method: "DELETE" });
