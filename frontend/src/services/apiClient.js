import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // Assurez-vous que les cookies sont inclus dans chaque requête
});

// Ajouter les interceptors pour inclure le token JWT dans chaque requête
apiClient.interceptors.request.use(
  config => {
    // Ne pas ajouter le token pour les requêtes de connexion et d'inscription
    if (!config.url.includes('/auth/login') && !config.url.includes('/auth/register')) {
      const token = sessionStorage.getItem('jwtToken');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);

export const login = async (email, password) => {
  try {
    const response = await apiClient.post('/auth/login', { email, password });
    const token = response.data.token;
    sessionStorage.setItem('jwtToken', token);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la connexion :', error);
    throw error;
  }
};

export const logout = async () => {
  try {
    sessionStorage.removeItem('jwtToken');
    window.location.href = '/login';
  } catch (error) {
    console.error('Erreur lors de la déconnexion :', error);
    throw error;
  }
};

export const getMenuGroupedByCategory = async () => {
  try {
    const response = await apiClient.get('user/menu/grouped');
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération du menu groupé :', error);
    throw error;
  }
};

export const getServerTodayReservations = async () => {
  try {
    const response = await apiClient.get('/server/reservations/today');
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des réservations du jour :', error);
    throw error;
  }
};

export default apiClient;