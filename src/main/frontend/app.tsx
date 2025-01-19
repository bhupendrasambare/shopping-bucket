import React, { useEffect, useState } from 'react';
import { RouterProvider } from 'react-router-dom';
import router from "Frontend/route"

const App: React.FC = () => {


    return (
        <div className="">
          
          <RouterProvider router={router} />
        </div>
    );
};

export default App;