import { useContext } from 'react'
import { TripContext } from '../context/TripContext'

export function useTripContext() {
  const context = useContext(TripContext)

  if (!context) {
    throw new Error('useTripContext должен использоваться внутри TripContextProvider')
  }

  return context
}
