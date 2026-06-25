import { useCallback, useEffect, useMemo, useState } from 'react'
import { fallbackTrips, fetchTrips } from '../services/tripsApi'

// Хук изолирует бизнес-логику работы с рейсами от UI-компонентов.
export function useTrips() {
  const [trips, setTrips] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [nextId, setNextId] = useState(100)

  useEffect(() => {
    let isMounted = true

    async function loadTrips() {
      try {
        const response = await fetchTrips()
        if (isMounted) {
          setTrips(response)
          const maxId = response.reduce((max, trip) => Math.max(max, Number(trip.id) || 0), 0)
          setNextId(maxId + 1)
        }
      } catch {
        if (isMounted) {
          setTrips(structuredClone(fallbackTrips))
          setError('Backend недоступен. Показаны демо-данные.')
          setNextId(100)
        }
      } finally {
        if (isMounted) {
          setIsLoading(false)
        }
      }
    }

    loadTrips()

    return () => {
      isMounted = false
    }
  }, [])

  const activeTrips = useMemo(
    () => trips.filter((trip) => trip.status === 'active'),
    [trips],
  )

  const archivedTrips = useMemo(
    () => trips.filter((trip) => trip.status === 'archive'),
    [trips],
  )

  const addTrip = useCallback(
    (tripData) => {
      const newTrip = {
        id: String(nextId),
        ...tripData,
        status: 'active',
      }

      setTrips((prevTrips) => [...prevTrips, newTrip])
      setNextId((prevId) => prevId + 1)
    },
    [nextId],
  )

  const deleteTrip = useCallback((tripId) => {
    setTrips((prevTrips) => prevTrips.filter((trip) => trip.id !== tripId))
  }, [])

  const archiveTrip = useCallback((tripId) => {
    setTrips((prevTrips) =>
      prevTrips.map((trip) =>
        trip.id === tripId ? { ...trip, status: 'archive' } : trip,
      ),
    )
  }, [])

  return {
    trips,
    activeTrips,
    archivedTrips,
    isLoading,
    error,
    addTrip,
    deleteTrip,
    archiveTrip,
  }
}
