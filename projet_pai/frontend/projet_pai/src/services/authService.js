import apiClient from './apiClient';

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

export const refreshToken = async () => {
  try {
    const response = await apiClient.post('/auth/refresh-token');
    const newToken = response.data.token;
    sessionStorage.setItem('jwtToken', newToken);
    return newToken;
  } catch (error) {
    console.error('Erreur lors du rafraîchissement du token :', error);
    throw error;
  }
};