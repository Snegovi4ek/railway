import PropTypes from 'prop-types'
import TripItem from './TripItem'

function TripList({ trips, onDelete, onArchive }) {
  if (!trips.length) {
    return <p className="empty-hint">Активных рейсов пока нет.</p>
  }

  return (
    <ol className="trip-list">
      {trips.map((trip) => (
        <TripItem
          key={trip.id}
          trip={trip}
          onDelete={onDelete}
          onArchive={onArchive}
        />
      ))}
    </ol>
  )
}

TripList.propTypes = {
  trips: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
    }),
  ).isRequired,
  onDelete: PropTypes.func.isRequired,
  onArchive: PropTypes.func.isRequired,
}

export default TripList
