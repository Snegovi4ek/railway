// Имитация API на случай, если backend не запущен.
export const fallbackTrips = [
  {
    id: '1',
    departureCity: 'Москва',
    arrivalCity: 'Санкт-Петербург',
    departureTime: '2026-06-10T08:00:00',
    arrivalTime: '2026-06-10T12:30:00',
    stationName: 'Ленинградский вокзал',
    status: 'archive',
  },
  {
    id: '2',
    departureCity: 'Казань',
    arrivalCity: 'Москва',
    departureTime: '2026-06-15T14:20:00',
    arrivalTime: '2026-06-15T22:45:00',
    stationName: 'Казанский вокзал',
    status: 'active',
  },
]

const API_URL = '/api/trips'

// Сервис загрузки начальных данных с backend.
export async function fetchTrips() {
  const response = await fetch(API_URL)

  if (!response.ok) {
    throw new Error('Не удалось загрузить рейсы')
  }

  return response.json()
}
