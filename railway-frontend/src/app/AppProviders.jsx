import PropTypes from 'prop-types'
import { TripContextProvider } from '../context/TripContext'

function AppProviders({ children }) {
  return <TripContextProvider>{children}</TripContextProvider>
}

AppProviders.propTypes = {
  children: PropTypes.node.isRequired,
}

export default AppProviders
