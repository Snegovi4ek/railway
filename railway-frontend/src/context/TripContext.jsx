import PropTypes from 'prop-types'
import { createContext } from 'react'
import { useTrips } from '../hooks/useTrips'

const TripContext = createContext(null)

export function TripContextProvider({ children }) {
  const tripsState = useTrips()

  return <TripContext.Provider value={tripsState}>{children}</TripContext.Provider>
}

TripContextProvider.propTypes = {
  children: PropTypes.node.isRequired,
}

export { TripContext }
