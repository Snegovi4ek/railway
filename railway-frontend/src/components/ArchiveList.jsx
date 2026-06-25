import PropTypes from 'prop-types'

function ArchiveList({ trips }) {
  if (!trips.length) {
    return <p className="empty-hint">Архив пуст.</p>
  }

  return (
    <ul className="archive-list">
      {trips.map((trip) => (
        <li key={trip.id}>
          {trip.departureCity} — {trip.arrivalCity}
        </li>
      ))}
    </ul>
  )
}

ArchiveList.propTypes = {
  trips: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      departureCity: PropTypes.string.isRequired,
      arrivalCity: PropTypes.string.isRequired,
    }),
  ).isRequired,
}

export default ArchiveList
