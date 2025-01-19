import { createBrowserRouter,RouteObject } from 'react-router-dom';
import NotFound from 'Frontend/pages/NotFound';
import Home from 'Frontend/pages/Home';
import Sales from 'Frontend/pages/Sales';
import Items from 'Frontend/pages/Items';

const routes: RouteObject[] = [
  { path: '/', element: <Home /> },
  { path: '/sales', element: <Sales /> },
  { path: '/items', element: <Items /> },
  { path: '*', element: <NotFound /> },
  ];
  
  const router = createBrowserRouter(routes);

export default router;