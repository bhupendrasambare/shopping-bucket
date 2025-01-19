import { createBrowserRouter,RouteObject } from 'react-router-dom';
import NotFound from 'Frontend/pages/NotFound';
import CreateSaleDetails from 'Frontend/pages/Sales';
import SalesHistory from 'Frontend/pages/SalesHistory';
import Items from 'Frontend/pages/Items';
import Dashboard from 'Frontend/pages/Dashboard';

const routes: RouteObject[] = [
  { path: '/', element: <Dashboard /> },
  { path: '/sales', element: <CreateSaleDetails /> },
  { path: '/history', element: <SalesHistory /> },
  { path: '/items', element: <Items /> },
  { path: '*', element: <NotFound /> },
  ];
  
  const router = createBrowserRouter(routes);

export default router;