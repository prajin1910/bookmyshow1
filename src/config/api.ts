const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Helper function to handle API responses
const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const errorText = await response.text();
    console.error('API Error:', response.status, errorText);
    throw new Error(`API Error: ${response.status} ${errorText}`);
  }
  return response;
};

export const api = {
  // Auth endpoints
  login: async (credentials: { username: string; password: string }) => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: credentials.username, password: credentials.password }),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Login API error:', error);
      throw error;
    }
  },

  register: async (userData: { username: string; email: string; password: string }) => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Register API error:', error);
      throw error;
    }
  },

  // Flight endpoints
  searchFlights: async (params: { departure: string; arrival: string; date?: string }) => {
    try {
      const searchParams = new URLSearchParams();
      searchParams.append('departure', params.departure);
      searchParams.append('arrival', params.arrival);
      if (params.date) searchParams.append('date', params.date);
      
      const response = await fetch(`${API_BASE_URL}/flights/search?${searchParams}`);
      return handleResponse(response);
    } catch (error) {
      console.error('Search flights API error:', error);
      throw error;
    }
  },

  getAvailableFlights: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights/available`);
      return handleResponse(response);
    } catch (error) {
      console.error('Get available flights API error:', error);
      throw error;
    }
  },

  getFlightById: async (id: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights/${id}`);
      return handleResponse(response);
    } catch (error) {
      console.error('Get flight by ID API error:', error);
      throw error;
    }
  },

  getAllFlights: async (token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights`, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Get all flights API error:', error);
      throw error;
    }
  },

  createFlight: async (flightData: any, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(flightData),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Create flight API error:', error);
      throw error;
    }
  },

  updateFlight: async (id: string, flightData: any, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(flightData),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Update flight API error:', error);
      throw error;
    }
  },

  deleteFlight: async (id: string, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/flights/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` },
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Delete flight API error:', error);
      throw error;
    }
  },

  // Booking endpoints
  createBooking: async (bookingData: any, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/bookings`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(bookingData),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Create booking API error:', error);
      throw error;
    }
  },

  getUserBookings: async (token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/bookings/user`, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Get user bookings API error:', error);
      throw error;
    }
  },

  getAllBookings: async (token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/bookings`, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Get all bookings API error:', error);
      throw error;
    }
  },

  updateBookingStatus: async (id: string, status: string, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/bookings/${id}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ status }),
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Update booking status API error:', error);
      throw error;
    }
  },

  downloadTicket: async (bookingId: string, token: string) => {
    try {
      const response = await fetch(`${API_BASE_URL}/bookings/${bookingId}/ticket`, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      return handleResponse(response);
    } catch (error) {
      console.error('Download ticket API error:', error);
      throw error;
    }
  },
};
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials),
    }),

  register: (userData: { username: string; email: string; password: string }) =>
    fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData),
    }),

  // Flight endpoints
  searchFlights: (params: { departure: string; arrival: string; date?: string }) => {
    const searchParams = new URLSearchParams();
    searchParams.append('departure', params.departure);
    searchParams.append('arrival', params.arrival);
    if (params.date) searchParams.append('date', params.date);
    
    return fetch(`${API_BASE_URL}/flights/search?${searchParams}`);
  },

  getAvailableFlights: () =>
    fetch(`${API_BASE_URL}/flights/available`),

  getFlightById: (id: string) =>
    fetch(`${API_BASE_URL}/flights/${id}`),

  getAllFlights: (token: string) =>
    fetch(`${API_BASE_URL}/flights`, {
      headers: { 'Authorization': `Bearer ${token}` },
    }),

  createFlight: (flightData: any, token: string) =>
    fetch(`${API_BASE_URL}/flights`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(flightData),
    }),

  updateFlight: (id: string, flightData: any, token: string) =>
    fetch(`${API_BASE_URL}/flights/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(flightData),
    }),

  deleteFlight: (id: string, token: string) =>
    fetch(`${API_BASE_URL}/flights/${id}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` },
    }),

  // Booking endpoints
  createBooking: (bookingData: any, token: string) =>
    fetch(`${API_BASE_URL}/bookings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(bookingData),
    }),

  getUserBookings: (token: string) =>
    fetch(`${API_BASE_URL}/bookings/user`, {
      headers: { 'Authorization': `Bearer ${token}` },
    }),

  getAllBookings: (token: string) =>
    fetch(`${API_BASE_URL}/bookings`, {
      headers: { 'Authorization': `Bearer ${token}` },
    }),

  updateBookingStatus: (id: string, status: string, token: string) =>
    fetch(`${API_BASE_URL}/bookings/${id}/status`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ status }),
    }),

  downloadTicket: (bookingId: string, token: string) =>
    fetch(`${API_BASE_URL}/bookings/${bookingId}/ticket`, {
      headers: { 'Authorization': `Bearer ${token}` },
    }),
};