import React from "react"
import { createRoot } from "react-dom/client"
import 'Frontend/themes/shopping-bucket/styles.css';
import App from "Frontend/app";
import 'bootstrap/dist/css/bootstrap.min.css';

const container = document.getElementById("outlet")

if (container) {
  const root = createRoot(container)

  root.render(
        <App />
    )
} else {
  throw new Error(
    "Root element with ID 'root' was not found in the document. Ensure there is a corresponding HTML element with the ID 'root' in your HTML file.",
  )
}