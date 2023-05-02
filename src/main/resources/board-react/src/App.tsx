import React, { useEffect, useState } from "react";
import logo from "./logo.svg";
import "./App.css";
import axios from "axios";

function App() {
    const [connection, setConnection] = useState<string>("");

    const connectionTest = () => {
        axios
            .get("/")
            .then((response) => {
                setConnection(response.data);
            })
            .catch((error) => {
                setConnection(error.message);
            });
    };

    useEffect(() => {
        connectionTest();
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <p>{connection}</p>
            </header>
        </div>
    );
}

export default App;
