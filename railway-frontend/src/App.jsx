import './App.css'
import ArchiveList from './components/ArchiveList'
import TripForm from './components/TripForm'
import TripList from './components/TripList'
import Footer from './components/layout/Footer'
import Header from './components/layout/Header'
import ErrorMessage from './components/ui/ErrorMessage'
import Loader from './components/ui/Loader'
import SectionCard from './components/ui/SectionCard'
import { useTripContext } from './hooks/useTripContext'

function App() {
  const {
    activeTrips,
    archivedTrips,
    isLoading,
    error,
    addTrip,
    deleteTrip,
    archiveTrip,
  } = useTripContext()

  return (
    <div className="app-shell">
      <Header />

      <main className="page-content">
        <div className="page-grid">
          <SectionCard title="Создание рейса">
            <p className="section-text">
              Заполните форму и нажмите «Создать» — рейс появится в списке активных.
            </p>
            <TripForm onCreate={addTrip} />
          </SectionCard>

          <SectionCard title="Активные рейсы">
            {isLoading ? <Loader /> : null}
            {error ? <ErrorMessage message={error} /> : null}
            {!isLoading ? (
              <TripList
                trips={activeTrips}
                onDelete={deleteTrip}
                onArchive={archiveTrip}
              />
            ) : null}
          </SectionCard>

          <SectionCard title="Архив">
            <p className="section-text">
              При архивировании маршрут попадает в этот список и исчезает из активных.
            </p>
            {!isLoading ? <ArchiveList trips={archivedTrips} /> : null}
          </SectionCard>
        </div>
      </main>

      <Footer />
    </div>
  )
}

export default App
