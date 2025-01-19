import { createBrowserRouter,RouteObject } from 'react-router-dom';
import NotFound from 'Frontend/pages/NotFound';
import Home from 'Frontend/pages/Home';
import SalesHistory from 'Frontend/pages/SalesHistory';
import Items from 'Frontend/pages/Items';

const routes: RouteObject[] = [
  { path: '/sales', element: <Home /> },
  { path: '/history', element: <SalesHistory /> },
  { path: '/items', element: <Items /> },
  { path: '*', element: <NotFound /> },
  ];
  
  const router = createBrowserRouter(routes);

export default router;