'use client'
import { useEffect, useState } from "react";
import {
  getAllParticipants,
  createParticipant,
  updateParticipant,
  deleteParticipant,
} from "../services/api";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client/dist/sockjs';
export default function Home() {
  const [participants, setParticipants] = useState([]);
  const [name, setName] = useState("");
  const [editing, setEditing] = useState(null);

  const fetchParticipants = async () => {
    const data = await getAllParticipants();
    setParticipants(data);
  };

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected to STOMP');

        stompClient.subscribe('/topic/participants', message => {
          console.log('Received message: ', message.body);
          fetchParticipants();
        });
      },
      onWebSocketError: (err) => console.error('WebSocket error:', err),
      onStompError: (err) => console.error('STOMP error:', err),
      debug: (str) => console.log(str),
    });

    stompClient.activate();

    return () => stompClient.deactivate();
  }, []);

  useEffect(() => {
    fetchParticipants();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editing) {
      await updateParticipant({ id: editing, name });
    } else {
      await createParticipant({ name });
    }
    setName("");
    setEditing(null);
    fetchParticipants();
  };

  const handleEdit = (participant) => {
    setEditing(participant.id);
    setName(participant.name);
  };

  const handleDelete = async (id) => {
    await deleteParticipant(id);
    fetchParticipants();
  };

  return (
      <div className="max-w-xl mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Participants</h1>

        <form onSubmit={handleSubmit} className="mb-6">
          <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Participant name"
              required
              className="border-2 rounded-xl p-2 w-full mb-2"
          />
          <button type="submit" className="bg-blue-600 border-2 rounded-4xl text-white px-4 py-2">
            {editing ? "Update" : "Add"} Participant
          </button>
        </form>

        <ul className="space-y-2">
          {participants.map((p) => (
              <li key={p.id} className="flex justify-between items-center border-2 rounded-xl p-2">
                <span>{p.name}</span>
                <div className="space-x-2">
                  <button
                      onClick={() => handleEdit(p)}
                      className="text-yellow-600 border rounded-xl border-yellow-600 px-2"
                  >
                    Edit
                  </button>
                  <button
                      onClick={() => handleDelete(p.id)}
                      className="text-red-600 border  border-2 rounded-xl border-red-600 px-2"
                  >
                    Delete
                  </button>
                </div>
              </li>
          ))}
        </ul>
      </div>
  );
}
