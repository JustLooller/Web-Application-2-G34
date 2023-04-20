import React from 'react';
import ReactDOM from 'react-dom/client';
import PAGES from "./pages/pages";
import './globals.css';

import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";


const router = createBrowserRouter(PAGES);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <RouterProvider router={router} />
  </React.StrictMode>
);

